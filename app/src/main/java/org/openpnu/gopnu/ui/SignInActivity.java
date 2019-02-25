package org.openpnu.gopnu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import org.openpnu.gopnu.R;

public class SignInActivity extends AppCompatActivity {

  private static final String TAG = SignInActivity.class.getSimpleName();
  private static final int RC_SIGN_IN = 9001;

  private SignInButton mSignInByGoogleButton;

  private FirebaseAuth mAuth;
  private GoogleSignInClient mGoogleSignInClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_in);

    mSignInByGoogleButton = findViewById(R.id.sign_in_by_google_button);
    mSignInByGoogleButton.setOnClickListener(this::onSignInByGoogleButtonClicked);

    mAuth = FirebaseAuth.getInstance();

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build();
    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
  }

  @Override
  protected void onResume() {
    super.onResume();

    FirebaseUser currentUser = mAuth.getCurrentUser();
    if (currentUser != null) {
      startMainActivity();
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == RC_SIGN_IN) {
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      try {
        GoogleSignInAccount account = task.getResult(ApiException.class);
        firebaseAuthWithGoogle(account);
      } catch (ApiException e) {
        Log.e(TAG, "onActivityResult: failed to sign in by google.", e);

        updateUI(null);
      }
    }
  }

  private void onSignInByGoogleButtonClicked(View view) {
    Log.d(TAG, "onSignInByGoogleButtonClicked() called with: view = [" + view + "]");

    signIn();
  }

  private void signIn() {
    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
    Log.d(TAG, "firebaseAuthWithGoogle() called with: account = [" + account + "]");

    if (account == null) {
      updateUI(null);
      return;
    }

    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
    mAuth.signInWithCredential(credential)
        .addOnCompleteListener(this, task -> {
          if (task.isSuccessful()) {
            Log.d(TAG, "fsignInWithCredential: success.");

            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
          } else {
            Log.e(TAG, "signInWithCredential: failed.", task.getException());

            updateUI(null);
          }
        });
  }


  private void updateUI(FirebaseUser user) {
    Log.d(TAG, "updateUI() called with: user = [" + user + "]");

    if (user != null) {
      mSignInByGoogleButton.setVisibility(View.GONE);

      startMainActivity();
    } else {
      mSignInByGoogleButton.setVisibility(View.VISIBLE);

      Snackbar.make(mSignInByGoogleButton, R.string.failed_to_sign_in, Snackbar.LENGTH_SHORT)
          .show();
    }
  }

  private void startMainActivity() {
    final Intent intent = new Intent(this, MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }
}

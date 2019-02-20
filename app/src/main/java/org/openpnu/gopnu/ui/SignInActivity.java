package org.openpnu.gopnu.ui;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import org.openpnu.gopnu.R;

import java.util.Arrays;
import java.util.List;

public class SignInActivity extends AppCompatActivity {

  private static final String TAG = SignInActivity.class.getSimpleName();
  private static final int RC_SIGN_IN = 9001;


  List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build());


  private FirebaseAuth mAuth;

  private MaterialButton mSignInByEmailButton;
  private SignInButton mSignInByGoogleButton;
  private GoogleSignInClient mGoogleSignInClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_in);

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();
    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    mSignInByEmailButton = findViewById(R.id.sign_in_by_email_button);
    mSignInByEmailButton.setOnClickListener(this::onSignInByEmailButtonClicked);

    mSignInByGoogleButton = findViewById(R.id.sign_in_by_google_button);
    mSignInByGoogleButton.setOnClickListener(this::onSignInByGoogleButtonClicked);

    mAuth = FirebaseAuth.getInstance();
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
        Log.w(TAG, "Google sign in failed", e);
        updateUI(null);
      }
    }
  }

  private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
    Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
    //final Intent intent = new Intent(this, 메인액티비티 추가바람);

    AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
    mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                  Log.d(TAG, "signInWithCredential:success");
                  FirebaseUser user = mAuth.getCurrentUser();
                  updateUI(user);
                  //startActivity(intent);              메인 액티비티 추가후 수정
                } else {
                  Log.w(TAG, "signInWithCredential:failure", task.getException());
                  Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                  updateUI(null);
                }
              }
            });
  }

  private void updateUI(FirebaseUser user) {
    if (user != null) {
      findViewById(R.id.sign_in_by_google_button).setVisibility(View.GONE);
    } else {
      findViewById(R.id.sign_in_by_google_button).setVisibility(View.VISIBLE);
    }
  }

  private void onSignInByGoogleButtonClicked(View view) {
    Log.d(TAG, "onSignInByGoogleButtonClicked() called with: view = [" + view + "]");
    signIn();
  }
  private void onSignInByEmailButtonClicked(View view) {
    Log.d(TAG, "onSignInByEmailButtonClicked() called with: view = [" + view + "]");
    final Intent intent = new Intent(this, SignInByEmailActivity.class);
    startActivity(intent);
  }
  private void signIn() {
    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }
}

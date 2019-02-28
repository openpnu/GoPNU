package org.openpnu.gopnu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import org.openpnu.gopnu.R;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

  private TextView mTitleView;

  private MaterialButton mSignOutButton;
  //private MaterialButton mRevokeButton; todo (@HWP) 회원 탈퇴
  private FirebaseAuth mAuth;

  private GoogleSignInClient mGoogleSignInClient;
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_profile, container, false);

    mSignOutButton = view.findViewById(R.id.sign_out_by_google_button);
    mSignOutButton.setOnClickListener(this::onSignOutByGoogleButtonClicked);

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();
    mGoogleSignInClient =GoogleSignIn.getClient(getContext(), gso);

    mAuth = FirebaseAuth.getInstance();

    /* todo (@HWP) 회원 탈퇴
    mRevokeButton = view.findViewById(R.id.revoke_by_google_button);
    mRevokeButton.setOnClickListener(this::onRevokeByGoogleButtonClicked);
    */

    return view;
  }

  private void onSignOutByGoogleButtonClicked(View view) {
    mAuth.signOut();

    final Intent intent = new Intent(getContext(), SignInActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);

    mGoogleSignInClient.signOut();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mTitleView = view.findViewById(R.id.profile_title);
  }

  /* todo (@HWP) 회원 탈퇴
  private void onRevokeByGoogleButtonClicked(View view) {
    mAuth.signOut();
    final Intent intent = new Intent(getContext(), SignInActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
    mGoogleSignInClient.revokeAccess();
  }
*/
}
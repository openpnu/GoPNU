package org.openpnu.gopnu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import org.openpnu.gopnu.R;

public class SignInByEmailActivity extends AppCompatActivity {

  private static final String TAG = SignInByEmailActivity.class.getSimpleName();

  private TextInputEditText mEmailEditText;
  private TextInputEditText mPasswordEditText;
  private MaterialButton mSignUpButton;
  private MaterialButton mSignInButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_in_by_email);

    mEmailEditText = findViewById(R.id.sign_in_by_email_email_edittext);
    mPasswordEditText = findViewById(R.id.sign_in_by_email_password_edittext);

    mSignUpButton = findViewById(R.id.sign_in_by_email_sign_up_button);
    mSignUpButton.setOnClickListener(this::onSignUpButtonClicked);

    mSignInButton = findViewById(R.id.sign_in_by_email_sign_in_button);
    mSignInButton.setOnClickListener(this::onSignInButtonClicked);
  }

  private void onSignUpButtonClicked(View view) {
    Log.d(TAG, "onSignUpButtonClicked() called with: view = [" + view + "]");
  }

  private void onSignInButtonClicked(View view) {
    Log.d(TAG, "onSignInButtonClicked() called with: view = [" + view + "]");

    // TODO(@ghkim3221): 이메일 주소로 로그인 기능 구현
    final Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
    finish();
  }
}

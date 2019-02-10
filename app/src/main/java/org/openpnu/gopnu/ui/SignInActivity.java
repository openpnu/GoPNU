package org.openpnu.gopnu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import org.openpnu.gopnu.R;

public class SignInActivity extends AppCompatActivity {

  private static final String TAG = SignInActivity.class.getSimpleName();

  private MaterialButton mSignInByEmailButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_in);

    mSignInByEmailButton = findViewById(R.id.sign_in_by_email_button);
    mSignInByEmailButton.setOnClickListener(this::onSignInByEmailButtonClicked);
  }

  private void onSignInByEmailButtonClicked(View view) {
    Log.d(TAG, "onSignInByEmailButtonClicked() called with: view = [" + view + "]");

    final Intent intent = new Intent(this, SignInByEmailActivity.class);
    startActivity(intent);
  }
}

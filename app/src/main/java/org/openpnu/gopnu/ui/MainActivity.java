package org.openpnu.gopnu.ui;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.openpnu.gopnu.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

  private BottomNavigationView mBottomNavigation;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    mBottomNavigation = findViewById(R.id.main_bottom_navigation);
    mBottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
      switch (menuItem.getItemId()) {
        case R.id.bottom_navigation_action_playing:
          replaceFragment(new PlayingFragment());
          return true;
        case R.id.bottom_navigation_action_popular:
          replaceFragment(new PopularFragment());
          return true;
        case R.id.bottom_navigation_action_profile:
          replaceFragment(new ProfileFragment());
          return true;
      }
      return false;
    });
    mBottomNavigation.setOnNavigationItemReselectedListener(menuItem -> {
      // TODO(@ghkim3221): 이미 선택된 네비게이션 항목을 다시 선택한 경우
    });
    mBottomNavigation.setSelectedItemId(R.id.bottom_navigation_action_popular);
    Button button = (Button) findViewById(R.id.Map_bt);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        startActivity(intent);
      }
    });


  }

  private void replaceFragment(@NonNull Fragment fragment) {
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.main_container, fragment)
        .commit();
  }


}

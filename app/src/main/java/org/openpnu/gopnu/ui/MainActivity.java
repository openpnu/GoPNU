package org.openpnu.gopnu.ui;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.openpnu.gopnu.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
  }

  private void replaceFragment(@NonNull Fragment fragment) {
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.main_container, fragment)
        .commit();
  }
}

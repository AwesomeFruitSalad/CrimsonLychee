package com.fruitsalad.crimsonkiwi;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.fruitsalad.crimsonkiwi.ui.Profile;
import com.fruitsalad.crimsonkiwi.ui.dashboard.DashboardFragment;
import com.fruitsalad.crimsonkiwi.ui.home.HomeFragment;
import com.fruitsalad.crimsonkiwi.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentTransaction.replace(R.id.nav_host_fragment,new HomeFragment()).commit();
                    break;
                case R.id.navigation_dashboard:
                    fragmentTransaction.replace(R.id.nav_host_fragment,new DashboardFragment()).commit();
                    break;
                case R.id.navigation_notifications:
                    fragmentTransaction.replace(R.id.nav_host_fragment,new NotificationsFragment()).commit();
                    break;
            }
            return true;
        }
    });

    }
}

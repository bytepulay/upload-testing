package asia.nainglintun.androidphpmysql.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import asia.nainglintun.androidphpmysql.Fragments.DetailUserFragment;
import asia.nainglintun.androidphpmysql.Fragments.FormFragment;
import asia.nainglintun.androidphpmysql.Fragments.PaymentFragment;
import asia.nainglintun.androidphpmysql.R;

public class ProfileActivity extends AppCompatActivity {
//    private TextView textViewUsername,textViewUserEmail;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    textViewUsername.setText(SharePrefManager.getInstance(getApplicationContext()).getUsername());
//                    textViewUserEmail.setText(SharePrefManager.getInstance(getApplicationContext()).getUserEmail());
                    loadFragment(new FormFragment());
                    return true;
                case R.id.navigation_dashboard:
                     loadFragment(new DetailUserFragment());
//                    textViewUsername.setText(SharePrefManager.getInstance(getApplicationContext()).getUsername());
//                    textViewUserEmail.setText(SharePrefManager.getInstance(getApplicationContext()).getUserEmail());
                    return true;
                case R.id.navigation_notifications:
                    loadFragment(new PaymentFragment());
//                    textViewUsername.setText(SharePrefManager.getInstance(getApplicationContext()).getUsername());
//                    textViewUserEmail.setText(SharePrefManager.getInstance(getApplicationContext()).getUserEmail());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        loadFragment(new FormFragment());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        //textViewUsername = findViewById(R.id.textViewUsername);
       // textViewUserEmail = findViewById(R.id.textViewUseremail);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

  public void getShareData(){
        if (SharePrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }
  }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace( R.id.form, fragment );
        //tx.addToBackStack( null );
        tx.commitNow();


    }


}

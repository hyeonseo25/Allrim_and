package com.example.allrim;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MyPageActivity extends AppCompatActivity {
    private FirebaseAuth mAuth ;
    private DrawerLayout mDrawerLayout;

    private TextView tv_nickname; // 닉네임 text
    private ImageView iv_profile; // 이미지 뷰

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);

        navigationView.getMenu().getItem(0).setChecked(true); // 페이지별로 바꾸기

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            mDrawerLayout.closeDrawers();

            int id = menuItem.getItemId();
            if(!menuItem.isChecked()){
                Intent intent;
                switch (id) {
                    case R.id.navigation_item_info:
                        intent = new Intent(this, MyPageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startActivity(intent);
                        break;
                    case R.id.navigation_item_writing:
                        Toast.makeText(MyPageActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.navigation_item_schedule:
                        Toast.makeText(MyPageActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.navigation_item_meal:
                        intent = new Intent(this, MealActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startActivity(intent);
                        break;
                    case R.id.navigation_item_lost:
                        Toast.makeText(MyPageActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.navigation_item_set:
                        Toast.makeText(MyPageActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                }
            }
            return false;
        });

        String nickName = mAuth.getCurrentUser().getDisplayName(); // MainActivity로 부터 닉네임 전달받음
        Uri photoUrl = mAuth.getCurrentUser().getPhotoUrl(); // MainActivity로 부터 프로필사진 Url 전달받음

        iv_profile = headerView.findViewById(R.id.img_userImage);
        Glide.with(this).load(photoUrl).into(iv_profile); // 프로필 url을 이미지 뷰에 세팅

        tv_nickname = (TextView) headerView.findViewById(R.id.tv_userName);
        tv_nickname.setText(nickName); // 닉네임 text를 텍스트 뷰에 세팅

        headerView.findViewById(R.id.bt_logout).setOnClickListener(onClickListener);
    }

    // 버튼 클릭 부분
    View.OnClickListener onClickListener = v -> {
        switch(v.getId()){
            case R.id.bt_logout:
                signOut();
                Intent intent = new Intent(getApplicationContext(), loginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    };

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        GoogleSignInClient googleApiClient = GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build());
        googleApiClient.signOut();
    }
}
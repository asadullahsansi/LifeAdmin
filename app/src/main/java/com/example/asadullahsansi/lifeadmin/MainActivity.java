package com.example.asadullahsansi.lifeadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by asadullahsansi on 11/9/17.
 */




public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth _auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _auth = FirebaseAuth.getInstance();

        findViewById(R.id.btnAddCategory).setOnClickListener(this);
        findViewById(R.id.btnAddSubCategory).setOnClickListener(this);
        findViewById(R.id.btnAddItem).setOnClickListener(this);
        findViewById(R.id.btnChat).setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sign_out_menu) {
            _auth.signOut();
            GoTo_Login();
        }
        return super.onOptionsItemSelected(item);
    }

    private void GoTo_Login() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser _currentUser = _auth.getCurrentUser();
        if (_currentUser == null) {
            GoTo_Login();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddCategory:
                startActivity(new Intent(MainActivity.this, CategoryActivity.class));
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                break;
            case R.id.btnAddSubCategory:
                startActivity(new Intent(MainActivity.this, SubCategoryActivity.class));
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

                break;

            case R.id.btnAddItem:
                startActivity(new Intent(MainActivity.this, ItemActivity.class));
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                break;
            case R.id.btnChat:
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                break;
        }

    }
}

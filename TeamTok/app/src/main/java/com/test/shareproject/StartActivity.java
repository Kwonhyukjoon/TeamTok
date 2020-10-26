package com.test.shareproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StartActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    Home home;
    ListBoard listBoard;
    SearchBoard searchBoard;
    QuestionBoard questionBoard;
    Mypage mypage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.tab1:
                        setFragment(0);
                        break;
                    case R.id.tab2:
                        setFragment(1);
                        break;
                    case R.id.tab3:
                        setFragment(2);
                        break;
                    case R.id.tab4:
                        setFragment(3);
                        break;
                    case R.id.tab5:
                        setFragment(4);
                        break;
                }
                return true;
            }
        });


        home = new Home();
        listBoard = new ListBoard();
        searchBoard = new SearchBoard();
        questionBoard = new QuestionBoard();
        mypage = new Mypage();
        setFragment(0); // 첫 프래그먼트 화면 지정
    }

    private void setFragment(int i) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (i) {
            case 0:
                ft.replace(R.id.Main_Frame, home);
                ft.commit();
                break;

            case 1:
                ft.replace(R.id.Main_Frame, listBoard);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case 2:
                ft.replace(R.id.Main_Frame, searchBoard);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case 3:
                ft.replace(R.id.Main_Frame, questionBoard);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case 4:
                ft.replace(R.id.Main_Frame, mypage);
                ft.addToBackStack(null);
                ft.commit();
                break;
        }
    }
}
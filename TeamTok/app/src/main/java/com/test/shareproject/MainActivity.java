package com.test.shareproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.shareproject.Dialog.CustomDialog;
import com.test.shareproject.User.JoinActivity;
import com.test.shareproject.User.LoginActivity;
import com.test.shareproject.Util.Utils;
import com.test.shareproject.api.NetworkClient;
import com.test.shareproject.api.UserApi;
import com.test.shareproject.model.UserReq;
import com.test.shareproject.model.UserRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Loading();
    }


    // 앱이 실행될 때 먼저 보여주는 로딩화면
    private void Loading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                // 로그인할때 생성되는 토큰값을 가져온다.
                sp = getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
                String token = sp.getString("token" , null);

                // 토큰값에 대한 조건
                if(token !=null){
                    Intent i = new Intent(MainActivity.this, StartActivity.class); // 메인 홈 화면으로 이동
                    startActivity(i);
                    finish();
                }else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class); // 로그인화면으로 이동
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000); // 2초동안 유지
    }
}
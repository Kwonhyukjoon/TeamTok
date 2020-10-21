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

        startLoading();
    }

    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                sp = getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
                String token = sp.getString("token" , null);
                if(token !=null){
                    Intent i = new Intent(MainActivity.this, StartActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }
}
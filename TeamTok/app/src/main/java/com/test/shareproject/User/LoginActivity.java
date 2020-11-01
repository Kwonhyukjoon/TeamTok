package com.test.shareproject.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.shareproject.Dialog.CustomDialog;
import com.test.shareproject.MainActivity;
import com.test.shareproject.R;
import com.test.shareproject.StartActivity;
import com.test.shareproject.Util.Utils;
import com.test.shareproject.api.NetworkClient;
import com.test.shareproject.api.UserApi;
import com.test.shareproject.model.UserReq;
import com.test.shareproject.model.UserRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    EditText LogEmail;
    EditText LogPwd;
    LinearLayout btnLogin;
    TextView btnNotlogin;
    TextView FindPwd;
    TextView btnResister;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    String email;
    String passwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        LogEmail = findViewById(R.id.LogEmail);
        LogPwd = findViewById(R.id.LogPwd);
        FindPwd = findViewById(R.id.FindPwd);
        btnLogin = findViewById(R.id.btnLogin);
        btnNotlogin = findViewById(R.id.btnNotlogin);
        btnResister = findViewById(R.id.btnResister);

        sp = getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
        editor = sp.edit();

        // 비밀번호 찾기
        // 비밀번호 찾기위한 다이얼로그 창 띄우기
        FindPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog customDialog = new CustomDialog(LoginActivity.this);
                customDialog.show();
            }
        });


        // 로그인 버튼
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = LogEmail.getText().toString().trim();
                passwd = LogPwd.getText().toString().trim();

                if(email.contains("@") == false){
                    Toast.makeText(LoginActivity.this,"이메일 형식이 올바르지 않습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }

//                if(passwd.isEmpty() || passwd.length() <4 || passwd.length() >12){
//                    Toast.makeText(MainActivity.this,"비밀번호 규칙에 맞지 않습니다.",Toast.LENGTH_SHORT).show();
//                    return;
//                }

                // Retrofit2 연결 - 로그인 API
                UserReq userReq = new UserReq(email,passwd);
                //네트워크로 데이터 처리
                Retrofit retrofit = NetworkClient.getRetrofitClient(LoginActivity.this);
                UserApi userApi = retrofit.create(UserApi.class);

                Call<UserRes> call = userApi.loginUser(userReq);

                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        // 로그인 성공했을때
                        if(response.isSuccessful()){
                            email = response.body().getEmail();
                            String token = response.body().getToken();
                            String nickname = response.body().getItems().get(0).getNickname();

                            // 쉐어러리펀스에 토큰, 이메일(아이디), 닉네임 값을 저장시킨다.
                            editor.putString("token",token);
                            editor.putString("email",email);
                            editor.putString("nickname",nickname);
                            editor.apply();

                            Intent loginintent = new Intent(LoginActivity.this, StartActivity.class);
                            startActivity(loginintent);

                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this,"아이디와 비밀번호를 확인해 주세요.",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {
                    }
                });
            }
        });

        // 비로그인으로 로그인 버튼
        btnNotlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // token값을 null값으로 저장시킨다.
                String token = null;
                editor.putString("token",token);
                editor.apply();
                Intent startintent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(startintent);

            }
        });

        // 회원가입 페이지로 이동하는 버튼
        btnResister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupintent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(signupintent);
            }
        });

    }

}
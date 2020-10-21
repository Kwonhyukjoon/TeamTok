package com.test.shareproject.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.shareproject.R;
import com.test.shareproject.Util.Utils;
import com.test.shareproject.api.NetworkClient;
import com.test.shareproject.api.UserApi;
import com.test.shareproject.model.UserReq;
import com.test.shareproject.model.UserRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyUpdateActivity extends AppCompatActivity {

    TextView myEmail;
    TextView myName;
    TextView myNick;
    EditText nowPwd;
    EditText myPwd;
    EditText samePwd;

    TextView txtPwd;
    TextView txtsamePwd;

    String email;
    String pwd;
    String samepwd;
    Boolean pwdlength;
    Boolean pwdChecked;

    TextView Check;

    SharedPreferences sp;
    String token;

    UserReq userReq;
    UserApi userApi;
    Retrofit retrofit;


    Toolbar toolbar;


    LinearLayout passLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_update);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
//        actionBar.setHomeAsUpIndicator(R.drawable.button_back); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요

        sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        token = sp.getString("token", null);

        myEmail = findViewById(R.id.myEmail);
        myName = findViewById(R.id.myName);
        myNick = findViewById(R.id.myNick);
        nowPwd = findViewById(R.id.nowPwd);
        myPwd = findViewById(R.id.myPwd);
        samePwd = findViewById(R.id.samePwd);
        txtPwd = findViewById(R.id.txtPwd);
        txtsamePwd = findViewById(R.id.txtsamePwd);
        passLayout = findViewById(R.id.passLayout);
        Check = findViewById(R.id.Check);

        userReq = (UserReq) getIntent().getSerializableExtra("myData");
        email = userReq.getEmail();
        String myname = userReq.getName();
        String mynickname = userReq.getNickname();

        myEmail.setText("" + email);
        myName.setText("" + myname);
        myNick.setText("" + mynickname);

        Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nowpwd = nowPwd.getText().toString().trim();

                userReq = new UserReq(email,nowpwd);
                retrofit = NetworkClient.getRetrofitClient(MyUpdateActivity.this);
                userApi = retrofit.create(UserApi.class);
                Call<UserRes> call = userApi.loginUser( userReq);
                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        Log.i("AAA", "!@#@#" + response.toString());
                        if(response.isSuccessful()){
                            Toast.makeText(MyUpdateActivity.this,"확인되었습니다.",Toast.LENGTH_SHORT).show();
                            nowPwd.setFocusable(false);
                            nowPwd.setEnabled(false);
                            Check.setEnabled(false);
                            passLayout.setVisibility(View.VISIBLE);
                        }
                        else {
                            Toast.makeText(MyUpdateActivity.this,"아이디와 비밀번호를 확인해 주세요.",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {
                        Log.i("AAA" , "!@#@#" + t.toString());

                    }
                });
            }
        });


        myPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pwd = myPwd.getText().toString().trim();
                if (pwd.length() >= 6 && pwd.length() <= 12) {
                    txtPwd.setTextColor(Color.BLUE);
                    txtPwd.setText("쓸 수 있는 비밀번호 입니다.");
                    pwdlength = true;
                } else {
                    txtPwd.setTextColor(Color.RED);
                    txtPwd.setText(" 비밀번호는 6~12자리");
                    pwdlength = false;
                }

                if(pwd.equals(samepwd)){
                    txtsamePwd.setTextColor(Color.BLUE);
                    txtsamePwd.setText(" 비밀번호가 같습니다.");
                    pwdChecked = true;
                }else{
                    txtsamePwd.setTextColor(Color.RED);
                    txtsamePwd.setText(" 비밀번호가 다릅니다.");
                    pwdChecked = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        samePwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (pwdlength == false) {
                    Toast.makeText(MyUpdateActivity.this, "알맞은 새로운 비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    myPwd.requestFocus();
                }else if(pwdlength == true){
                    txtsamePwd.setVisibility(View.VISIBLE);
                }
            }
        });

        samePwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                samepwd = samePwd.getText().toString().trim();
                if (samepwd.equals(pwd)) {
                    txtsamePwd.setTextColor(Color.BLUE);
                    txtsamePwd.setText(" 비밀번호가 같습니다.");
                    pwdChecked = true;
                } else {
                    txtsamePwd.setTextColor(Color.RED);
                    txtsamePwd.setText(" 비밀번호가 다릅니다.");
                    pwdChecked = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            // 변경된 내용 저장
            case R.id.btn_update:

                if(pwdChecked == false){
                    Toast.makeText(MyUpdateActivity.this,"비밀번호가 다릅니다!",Toast.LENGTH_SHORT).show();
                }

                String udpwd = myPwd.getText().toString().trim();
                String sampwd = samePwd.getText().toString().trim();

                if(udpwd.isEmpty() || sampwd.isEmpty()){
                    Toast.makeText(MyUpdateActivity.this,"새로운 비밀번호를 알맞게 작성해 주세요.",Toast.LENGTH_SHORT).show();
                    return false;
                }


                userReq = new UserReq(email,udpwd);
                retrofit = NetworkClient.getRetrofitClient(MyUpdateActivity.this);
                userApi = retrofit.create(UserApi.class);
                Call<UserRes> passwdcall = userApi.resetPasswd(userReq);
                passwdcall.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        if(response.isSuccessful()){
                            Log.i("AAA",response.toString());
                            Toast.makeText(MyUpdateActivity.this,"수정 완료!",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {
                        Log.i("AAA" , "!@#@#" + t.toString());
                    }
                });

                break;
            // 뒤로 가기
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}



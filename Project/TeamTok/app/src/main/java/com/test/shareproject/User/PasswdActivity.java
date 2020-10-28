package com.test.shareproject.User;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.test.shareproject.R;
import com.test.shareproject.User.JoinActivity;
import com.test.shareproject.api.NetworkClient;
import com.test.shareproject.api.UserApi;
import com.test.shareproject.model.UserReq;
import com.test.shareproject.model.UserRes;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PasswdActivity extends AppCompatActivity {

    EditText resetPass1;
    EditText resetPass2;
    Button btnResetPass;

    TextView txtchange2;
    TextView txtsame2;

    String pass1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwd);

        resetPass1 = findViewById(R.id.resetPass1);
        resetPass2 = findViewById(R.id.resetPass2);
        btnResetPass = findViewById(R.id.btnResetPass);
        txtchange2 = findViewById(R.id.txtchange2);
        txtsame2 = findViewById(R.id.txtsame2);

        Intent i = getIntent();
        String email = i.getStringExtra("email");
        Log.i("BBB" , "!@#@!#" + email);

        resetPass1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pass1 = resetPass1.getText().toString().trim();
                if (pass1.length() >= 2 && pass1.length() <= 3) {
                    txtchange2.setTextColor(Color.BLUE);
                    txtchange2.setText("쓸 수 있는 비밀번호 입니다.");
                } else {
                    txtchange2.setTextColor(Color.RED);
                    txtchange2.setText(" 비밀번호는 6~12자리");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        resetPass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String samepass2 = resetPass2.getText().toString().trim();
                if (samepass2.equals(pass1)) {
                    txtsame2.setTextColor(Color.BLUE);
                    txtsame2.setText(" 비밀번호가 같습니다.");
                    Log.i("AAA",samepass2 +" "+pass1);
                } else {
                    txtsame2.setTextColor(Color.RED);
                    txtsame2.setText(" 비밀번호가 다릅니다.");
                    Log.i("AAA",samepass2 +" "+pass1);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("AAA","버튼눌림");
                String resetpass = resetPass1.getText().toString().trim();
                String resetpass2 = resetPass2.getText().toString().trim();

                if (resetpass.equals(resetpass2)){
                    UserReq userReq = new UserReq(email,resetpass);
                    //네트워크로 데이터 처리
                    Retrofit retrofit = NetworkClient.getRetrofitClient(PasswdActivity.this);
                    UserApi userApi = retrofit.create(UserApi.class);

                    Call<UserRes> call = userApi.resetPasswd(userReq);
                    call.enqueue(new Callback<UserRes>() {
                        @Override
                        public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                            if(response.isSuccessful()){
                                Log.i("AAA","id,pass : " + resetpass + email);
                                Log.i("AAA","패스워드바꾸는곳 : " + response.toString());
                                Intent i = new Intent(PasswdActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();
                            }
                            Log.i("AAA","패스워드바꾸는곳qkR : " + response.toString());
                        }

                        @Override
                        public void onFailure(Call<UserRes> call, Throwable t) {
                            Log.i("AAA",t.toString());
                        }
                    });
                }

            }
        });

    }
}
package com.test.shareproject.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.shareproject.R;
import com.test.shareproject.MainActivity;
import com.test.shareproject.User.LoginActivity;
import com.test.shareproject.User.MyUpdateActivity;
import com.test.shareproject.Util.Utils;
import com.test.shareproject.api.NetworkClient;
import com.test.shareproject.api.UserApi;
import com.test.shareproject.model.UserReq;
import com.test.shareproject.model.UserRes;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class MypageDialog extends Dialog {
    private Context mContext;
    LinearLayout btnlogout;
    LinearLayout btnUpdate;
    private TextView myNick;
    private TextView Cancel;
    private TextView Check;
    private TextView txtCheck;
    ImageView reset;

    String token;
    SharedPreferences sp;
    String mynickname;
    String nickname;

    int nickChecked=0;

    Retrofit retrofit;
    Call<UserRes> call;
    UserApi userApi;

    UserReq userReq;
    ArrayList<UserReq> userReqArrayList = new ArrayList<>();

    private NickDialogListener nickDialogListener;

    public MypageDialog(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    //인터페이스 설정
    public interface NickDialogListener{
        void clickbtn(String nickname);
    }

    public void setNickDialogListener(NickDialogListener nickDialogListener){
        this.nickDialogListener = nickDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypagedialog);

        sp = mContext.getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        token = sp.getString("token", null);
        mynickname = sp.getString("nickname", null);

        // 다이얼로그의 배경을 투명으로 만든다.
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        btnUpdate = findViewById(R.id.btnUpdate);
        btnlogout = findViewById(R.id.btnlogout);
        myNick = findViewById(R.id.myNick);
        Cancel = findViewById(R.id.Cancel);
        Check = findViewById(R.id.Check);
        reset = findViewById(R.id.reset);
        txtCheck = findViewById(R.id.txtCheck);

        myNick.setText(""+mynickname);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = NetworkClient.getRetrofitClient(mContext);
                UserApi userApi = retrofit.create(UserApi.class);
                Call<UserRes> call = userApi.Mypage("Bearer " + token);
                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        Intent i = new Intent(mContext,MyUpdateActivity.class);
                        userReqArrayList = response.body().getResult();
                        userReq = userReqArrayList.get(0);
                        i.putExtra("myData",userReq);
                        mContext.startActivity(i);
                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {

                    }
                });
            }
        });

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = NetworkClient.getRetrofitClient(mContext);
                UserApi userApi = retrofit.create(UserApi.class);
                Call<UserRes> call = userApi.logout("Bearer " + token);

                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        Log.i("AAA" , "!@#@#" + response.toString());
                        Log.i("AAA" , "!@#@#" + token);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("token", null);
                        editor.putString("email" , null);
                        editor.apply();

                        Intent i = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(i);
                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {

                    }
                });
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myNick.setText("");
            }
        });

        myNick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nickname = myNick.getText().toString().trim();

                if (nickname.isEmpty()) {
                    txtCheck.setVisibility(View.INVISIBLE);
                    nickChecked = 0;
                    return;
                }

                userReq = new UserReq(nickname,0);
                retrofit = NetworkClient.getRetrofitClient(mContext);
                userApi = retrofit.create(UserApi.class);
                call = userApi.checkNickName(userReq);

                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        Log.i("AAA" , "!@#" + nickname + "," + mynickname);
                        if (response.isSuccessful()) {
                            txtCheck.setText("사용가능한 닉네임 입니다.");
                            txtCheck.setTextColor(Color.BLUE);
                            txtCheck.setVisibility(View.VISIBLE);
                            nickChecked = 1;
                        } else if(nickname.equals(mynickname)){
                                txtCheck.setVisibility(View.VISIBLE);
                                txtCheck.setText("현재 닉네임입니다.");
                                txtCheck.setTextColor(Color.BLACK);
                            }else {
                            txtCheck.setText("중복된 닉네임 입니다.");
                            txtCheck.setTextColor(Color.RED);
                            txtCheck.setVisibility(View.VISIBLE);
                            nickChecked = 0;
                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {
                        Log.i("AAA", "!#!@#" + t.toString());
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("AAA" , "!@##@" + nickname + "," + mynickname);
                if(nickname == null){
                    dismiss();
                    return;
                }
                if (nickname.isEmpty()) {
                    Toast.makeText(mContext, "닉네임을 작성해 주세요!", Toast.LENGTH_SHORT).show();
                    nickChecked = 0;
                    return;
                }
                if (nickChecked == 0) {
                    Toast.makeText(mContext, "중복된 닉네임 입니다.", Toast.LENGTH_SHORT).show();
                    nickname = null;
                    return;
                }else {
                    nickname = myNick.getText().toString().trim();

                    userReq = new UserReq(nickname,0);
                    retrofit = NetworkClient.getRetrofitClient(mContext);
                    userApi = retrofit.create(UserApi.class);
                    call = userApi.changeMyNik("Bearer " + token , userReq);

                    call.enqueue(new Callback<UserRes>() {
                        @Override
                        public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                            nickDialogListener.clickbtn(nickname);
                            sp = mContext.getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("nickname",nickname);
                            editor.apply();
                            Toast.makeText(mContext,"수정 완료!",Toast.LENGTH_SHORT).show();
                            dismiss();
                        }

                        @Override
                        public void onFailure(Call<UserRes> call, Throwable t) {

                        }
                    });

                    dismiss();
                }
            }
        });


        // 버튼 리스너 설정
        Cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '취소' 버튼 클릭시
                // Custom Dialog 종료
                dismiss();
            }
        });

    }




}

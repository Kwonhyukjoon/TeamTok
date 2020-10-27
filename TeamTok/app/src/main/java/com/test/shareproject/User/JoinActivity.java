package com.test.shareproject.User;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.test.shareproject.MainActivity;
import com.test.shareproject.R;
import com.test.shareproject.Util.GMailSender;
import com.test.shareproject.Util.Utils;
import com.test.shareproject.api.NetworkClient;
import com.test.shareproject.api.UserApi;
import com.test.shareproject.model.UserReq;
import com.test.shareproject.model.UserRes;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JoinActivity extends AppCompatActivity implements View.OnClickListener, Dialog.OnCancelListener {


    EditText txtPass1;
    EditText txtPass2;
    EditText txtNick;
    EditText txtName;
    TextView txtchange;
    TextView txtsame;



    ProgressDialog progressDialog;
    GMailSender gMailSender;
    String id;
    String pass;
    String code;
    Button btn_send;
    private Button btnNick;
    private EditText et_ID;
    private Button btn_join = null;


    LayoutInflater dialog; //LayoutInflater
    View dialogLayout; //layout을 담을 View
    Dialog authDialog; //dialog 객체

    TextView time_counter; //시간을 보여주는 TextView
    EditText emailAuth_number; //인증 번호를 입력 하는 칸
    Button emailAuth_btn; // 인증버튼

    CountDownTimer countDownTimer;
    final int MILLISINFUTURE = 180 * 1000; //총 시간 (300초 = 5분)
    final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)

    FloatingActionButton fab;

    boolean isChecked = false; // 이메일 중복체크
    boolean nickChecked = false; // 이메일 중복체크
    boolean success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        //메일
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);

        btnNick = (Button)findViewById(R.id.btnNick);
        btnNick.setOnClickListener(this);

        et_ID = (EditText) findViewById(R.id.et_ID);

        btn_join = (Button) findViewById(R.id.btn_join);
        btn_join.setOnClickListener(this);

        fab = findViewById(R.id.fab);


        txtPass1 = findViewById(R.id.txtPass1);
        txtPass2 = findViewById(R.id.txtPass2);
        txtNick = findViewById(R.id.txtNick);
        txtName = findViewById(R.id.txtName);
        txtchange = findViewById(R.id.txtchange);
        txtsame = findViewById(R.id.txtsame);


        txtPass1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pass = txtPass1.getText().toString().trim();
                if (pass.length() >= 6 && pass.length() <= 12) {
                    txtchange.setTextColor(Color.BLUE);
                    txtchange.setText("쓸 수 있는 비밀번호 입니다.");
                } else {
                    txtchange.setTextColor(Color.RED);
                    txtchange.setText(" 비밀번호는 6~12자리");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtPass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String samepass = txtPass2.getText().toString().trim();
                if (samepass.equals(pass)) {
                    txtsame.setTextColor(Color.BLUE);
                    txtsame.setText(" 비밀번호가 같습니다.");
                } else {
                    txtsame.setTextColor(Color.RED);
                    txtsame.setText(" 비밀번호가 다릅니다.");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void countDownTimer() { //카운트 다운 메소드

        time_counter = (TextView) dialogLayout.findViewById(R.id.emailAuth_time_counter);
        //줄어드는 시간을 나타내는 TextView
        emailAuth_number = (EditText) dialogLayout.findViewById(R.id.emailAuth_number);
        //사용자 인증 번호 입력창
        emailAuth_btn = (Button) dialogLayout.findViewById(R.id.emailAuth_btn);
        //인증하기 버튼


        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) { //(300초에서 1초 마다 계속 줄어듬)

                long emailAuthCount = millisUntilFinished / 1000;
                Log.d("Alex", emailAuthCount + "");

                if ((emailAuthCount - ((emailAuthCount / 60) * 60)) >= 10) { //초가 10보다 크면 그냥 출력
                    time_counter.setText((emailAuthCount / 60) + " : " + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...
                    time_counter.setText((emailAuthCount / 60) + " : 0" + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                }

                //emailAuthCount은 종료까지 남은 시간임. 1분 = 60초 되므로,
                // 분을 나타내기 위해서는 종료까지 남은 총 시간에 60을 나눠주면 그 몫이 분이 된다.
                // 분을 제외하고 남은 초를 나타내기 위해서는, (총 남은 시간 - (분*60) = 남은 초) 로 하면 된다.

            }


            @Override
            public void onFinish() { //시간이 다 되면 다이얼로그 종료

                authDialog.dismiss();


            }
        }.start();

        emailAuth_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // 이메일 중복버튼
            case R.id.btn_send:
                id = et_ID.getText().toString().trim();

                UserReq userReq = new UserReq(id);
                if (id.isEmpty()) {
                    Toast.makeText(JoinActivity.this, R.string.login_err_1, Toast.LENGTH_SHORT).show();
                    return;
                }

                Retrofit retrofit = NetworkClient.getRetrofitClient(JoinActivity.this);
                UserApi userApi = retrofit.create(UserApi.class);

                Call<UserRes> call = userApi.checkId(userReq);

                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        if (response.isSuccessful()) {
                            success = response.body().isSuccess();
                            Log.i("AAA", "success : " + success);


                            code = getRandom(4); // 8글자의 랜덤 문자열을 이메일로 받아온다.
                            gMailSender = new GMailSender(Utils.ID, Utils.PASSWORD);
                            timeThread();
                            Log.i("AAA", "인증번호 : " + code);

                        } else {
                            Toast.makeText(JoinActivity.this, "아이디가 이미 사용중입니다.", Toast.LENGTH_SHORT).show();
                            et_ID.setText("");
                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {
                        Log.i("AAA", "Fail msg : " + t.getMessage());
                    }
                });

                break;

            case R.id.emailAuth_btn: //다이얼로그 내의 인증번호 인증 버튼을 눌렀을 시
                String emailAuth = emailAuth_number.getText().toString().trim();
                if (emailAuth.equalsIgnoreCase(code)) {
                    Toast.makeText(JoinActivity.this, "인증 성공", Toast.LENGTH_SHORT).show();
                    isChecked = true;
                    runOnUiThread(new Runnable() {
                        //중복확인 후 아이디 변경 못하게막기
                        @Override
                        public void run() {
                            if (success == true && isChecked == true) {

                                et_ID.setFocusable(false);
                                et_ID.setEnabled(false);
                                btn_send.setEnabled(false);
                            }else {
                                return;
                            }
                        }
                    });
                    if (authDialog != null && authDialog.isShowing()) {
                        authDialog.dismiss();
                        authDialog = null;
                    }
                } else {
                    Toast.makeText(JoinActivity.this, "인증 번호를 다시 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    emailAuth_number.setText("");
                }
                break;

            // 닉네임 중복ㅎ버튼
            case R.id.btnNick:
                String nickname = txtNick.getText().toString().trim();

                UserReq nickReq = new UserReq(nickname,0);

                Retrofit nikretrofit = NetworkClient.getRetrofitClient(JoinActivity.this);
                userApi = nikretrofit.create(UserApi.class);

                Call<UserRes> nikcall = userApi.checkNickName(nickReq);

                nikcall.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        if (response.isSuccessful()) {
                            success = response.body().isSuccess();
                            nickChecked = true;
                        } else {
                            Toast.makeText(JoinActivity.this, "중복", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        runOnUiThread(new Runnable() {
                            //중복확인 후 닉네임 변경 못하게막기
                            @Override
                            public void run() {
                                if (success == true && nickChecked == true) {
                                    txtNick.setFocusable(false);
                                    txtNick.setEnabled(false);
                                    btnNick.setEnabled(false);
                                }else {
                                    return;
                                }
                            }
                        });
                    }
                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.i("AAA", "Fail msg : " + t.getMessage());
                    }
                });
                break;

            case R.id.btn_join:
                String join_email = et_ID.getText().toString().trim();
                String passwd = txtPass1.getText().toString().trim();
                String join_Nik = txtNick.getText().toString().trim();
                String name = txtName.getText().toString().trim();


                if (isChecked == true && nickChecked == true){
                    userReq = new UserReq(join_email,passwd,join_Nik,name);
                    retrofit =  NetworkClient.getRetrofitClient(JoinActivity.this);
                    userApi = retrofit.create(UserApi.class);
                    Call<UserRes> joincall = userApi.createUser(userReq);

                    joincall.enqueue(new Callback<UserRes>() {
                        @Override
                        public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                            if (response.isSuccessful()){
                                Toast.makeText(JoinActivity.this, "완료.", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(JoinActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserRes> call, Throwable t) {

                        }
                    });
                }

        }

    }

    public void timeThread() {
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(JoinActivity.this);
                progressDialog.setTitle("Wait...");
                progressDialog.setMessage("인증번호를 보내는 중 입니다.");
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(true);
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            gMailSender.sendMail("Teamtok에서 보낸 회원가입 인증번호 입니다.", code, id);
                            sleep(2000);
                        } catch (Exception e) {

                        }
                        progressDialog.dismiss();
                        Looper.prepare();
                        Dialog();
                        Toast.makeText(JoinActivity.this, "이메일이 성공적으로 보내졌습니다.", Toast.LENGTH_SHORT).show();


                        Looper.loop();


                    }

                    private void sleep(int i) {
                    }
                }).start();
            }
        }, 0);
    }


    public void Dialog() {
        dialog = LayoutInflater.from(this);
        dialogLayout = dialog.inflate(R.layout.auth_dialog, null); // LayoutInflater를 통해 XML에 정의된 Resource들을 View의 형태로 반환 시켜 줌
        authDialog = new Dialog(this); //Dialog 객체 생성
        authDialog.setContentView(dialogLayout); //Dialog에 inflate한 View를 탑재 하여줌
        authDialog.setCanceledOnTouchOutside(false); //Dialog 바깥 부분을 선택해도 닫히지 않게 설정함.
        authDialog.show(); //Dialog를 나타내어 준다.
        countDownTimer();
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        countDownTimer.cancel();
    } //다이얼로그 닫을 때 카운트 다운 타이머의 cancel()메소드 호출

    // 인증번호를 랜덤 문자열로 가져오는 함수다.
    private static final char[] chars;

    static {
        StringBuilder builder = new StringBuilder();
        for (char ch = '0'; ch <= '1'; ++ch)
            builder.append(ch);
        for (char ch = 'a'; ch <= 'z'; ++ch)
            builder.append(ch);
        for (char ch = 'A'; ch <= 'Z'; ++ch)
            builder.append(ch);
        chars = builder.toString().toCharArray();
    }

    public static String getRandom(int length) {
        if (length < 1)
            throw new IllegalArgumentException("length < 1 : " + length);
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            randomString.append(chars[random.nextInt(chars.length)]);
        }
        return randomString.toString();
    }

}
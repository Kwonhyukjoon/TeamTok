package com.test.shareproject.Dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Response;
import com.test.shareproject.R;
import com.test.shareproject.User.PasswdActivity;
import com.test.shareproject.Util.GMailSender;
import com.test.shareproject.Util.Utils;
import com.test.shareproject.api.NetworkClient;
import com.test.shareproject.api.UserApi;
import com.test.shareproject.model.UserReq;
import com.test.shareproject.model.UserRes;

import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class CustomDialog extends Dialog implements View.OnClickListener, Dialog.OnCancelListener {

    private EditText txtEmail;
    private TextView findPw;
    private TextView Cancel;
    private Context mContext;


    ProgressDialog progressDialog;
    GMailSender gMailSender;
    String id;
    String code;
    String email;

    LayoutInflater dialog; //LayoutInflater
    View dialogLayout; //layout을 담을 View
    Dialog authDialog; //dialog 객체

    TextView time_counter; //시간을 보여주는 TextView
    EditText emailAuth_number; //인증 번호를 입력 하는 칸
    Button emailAuth_btn; // 인증버튼

    CountDownTimer countDownTimer;
    final int MILLISINFUTURE = 180 * 1000; //총 시간 (300초 = 5분)
    final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customdialog);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        // 다이얼로그의 배경을 투명으로 만든다.
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        txtEmail = findViewById(R.id.txtEmail);
        findPw = findViewById(R.id.findPw);
        findPw.setOnClickListener(this);
        Cancel = findViewById(R.id.Cancel);
        Cancel.setOnClickListener(this);

    }

    public CustomDialog(Context mContext) {
        super(mContext);
        this.mContext = mContext;
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
                authDialog.cancel();
            }
        }.start();

        emailAuth_btn.setOnClickListener(this);
    }

    public void timeThread() {
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setTitle("Wait...");
                progressDialog.setMessage("인증번호를 보내는 중 입니다.");
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(true);
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            id = txtEmail.getText().toString().trim(); // c추가한거임
                            gMailSender.sendMail("Teamtok에서 보낸 회원가입 인증번호 입니다.", code, id); // >여기서 안보내짐(아이디안들어가짐)
                            Log.i("AAA","id : " + id);
                            sleep(2000);
                        } catch (Exception e) {

                        }
                        progressDialog.dismiss();
                        Looper.prepare();
                        Dialog();
                        Toast.makeText(mContext, "이메일이 성공적으로 보내졌습니다.", Toast.LENGTH_SHORT).show();


                        Looper.loop();


                    }

                    private void sleep(int i) {
                    }
                }).start();
            }
        }, 0);
    }


    public void Dialog() {
        dialog = LayoutInflater.from(mContext);
        dialogLayout = dialog.inflate(R.layout.auth_dialog, null); // LayoutInflater를 통해 XML에 정의된 Resource들을 View의 형태로 반환 시켜 줌
        authDialog = new Dialog(mContext); //Dialog 객체 생성
        authDialog.setContentView(dialogLayout); //Dialog에 inflate한 View를 탑재 하여줌
        authDialog.setCanceledOnTouchOutside(false); //Dialog 바깥 부분을 선택해도 닫히지 않게 설정함.
        authDialog.show(); //Dialog를 나타내어 준다.
        countDownTimer();
    }

    // 버튼 이벤트
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.findPw:
                email = txtEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(mContext, "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                UserReq userReq = new UserReq(email);
                Retrofit retrofit = NetworkClient.getRetrofitClient(getContext());
                UserApi userApi = retrofit.create(UserApi.class);

                Call<UserRes> call = userApi.checkId(userReq);

                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, retrofit2.Response<UserRes> response) {
                        if (response.isSuccessful()) {
                            Log.i("AAA", "여기임당 : " + response.toString());
                            Toast.makeText(getContext(), "아이디가 존재하지않음", Toast.LENGTH_SHORT).show();
                            txtEmail.setText("");
                        } else {
                            Toast.makeText(getContext(), "넘어가셈", Toast.LENGTH_SHORT).show();
                            Log.i("AAA", "여기임당 : " + response.toString());
                            code = getRandom(2); // 8글자의 랜덤 문자열을 이메일로 받아온다.
                            gMailSender = new GMailSender(Utils.ID, Utils.PASSWORD);
                            timeThread();
                            Log.i("AAA", "인증번호 : " + code);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {

                    }
                });
                break;

            case R.id.emailAuth_btn: //다이얼로그 내의 인증번호 인증 버튼을 눌렀을 시
                String emailAuth = emailAuth_number.getText().toString().trim();
                if (emailAuth.equalsIgnoreCase(code)) {
                    Toast.makeText(mContext, "인증 성공", Toast.LENGTH_SHORT).show();
                    if (authDialog != null && authDialog.isShowing()) {
                        authDialog.cancel();
                        authDialog = null;
                        Intent i = new Intent(getContext(), PasswdActivity.class);
                        i.putExtra("email",email);
                        getContext().startActivity(i);
                        dismiss();
                    }
                } else {
                    Toast.makeText(mContext, "인증 번호를 다시 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    emailAuth_number.setText("");
                }
                break;
            case R.id.Cancel:
                dismiss();
        }
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
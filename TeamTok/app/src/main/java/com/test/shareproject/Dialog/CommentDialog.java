package com.test.shareproject.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.shareproject.Board.DetailActivity;
import com.test.shareproject.R;
import com.test.shareproject.Util.Utils;
import com.test.shareproject.adapter.CommentAdapter;
import com.test.shareproject.api.CommentApi;
import com.test.shareproject.api.NetworkClient;
import com.test.shareproject.model.BoardReq;
import com.test.shareproject.model.CommentReq;
import com.test.shareproject.model.CommentRes;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class CommentDialog extends Dialog {
    private Context mContext;

    private TextView Cancel;
    private TextView Check;
    ImageView reset;
    EditText txtComment;

    int cmt_no;
    int board_id;

    ArrayList<CommentReq> commentReqArrayList = new ArrayList<>();

    private CommentDialogListener commentDialogListener;

    //인터페이스 설정
    public interface CommentDialogListener{
        void clickbtn(int board_id);
    }

    public void setCommentDialogListener(CommentDialog.CommentDialogListener commentDialogListener){
        this.commentDialogListener = commentDialogListener;
    }


    public CommentDialog(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_dialog);

        Cancel = findViewById(R.id.Cancel);
        Check = findViewById(R.id.Check);
        reset = findViewById(R.id.reset);
        txtComment = findViewById(R.id.txtComment);

        Log.i("AAA" , "!@#@# 보드아이디 : " + board_id);

        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtComment.setText("");
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

    private void addCommentdata() {
        Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = txtComment.getText().toString().trim();
                if (comment.isEmpty()) {
                    Toast.makeText(mContext, "내용을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }


                CommentReq addReq = new CommentReq(cmt_no, board_id, comment);
                Retrofit retrofit = NetworkClient.getRetrofitClient(mContext);
                CommentApi commentApi = retrofit.create(CommentApi.class);

                SharedPreferences sp = mContext.getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                String token = sp.getString("token", null);

                Call<CommentRes> call = commentApi.addComment("Bearer " + token, addReq);
                call.enqueue(new Callback<CommentRes>() {
                    @Override
                    public void onResponse(Call<CommentRes> call, Response<CommentRes> response) {
                        Log.i("AAA" , "!@#@#" + response.body().toString());
                        commentReqArrayList = response.body().getItems();
                    }

                    @Override
                    public void onFailure(Call<CommentRes> call, Throwable t) {
                    }
                });
            }
        });

    }

}

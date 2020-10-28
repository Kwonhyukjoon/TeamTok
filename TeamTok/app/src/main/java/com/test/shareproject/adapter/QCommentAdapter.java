package com.test.shareproject.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.provider.Contacts;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.test.shareproject.Board.DetailActivity;
import com.test.shareproject.Question.QuestionDetail;
import com.test.shareproject.R;
import com.test.shareproject.Util.Utils;
import com.test.shareproject.api.CommentApi;
import com.test.shareproject.api.NetworkClient;
import com.test.shareproject.model.CommentReq;
import com.test.shareproject.model.CommentRes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class QCommentAdapter extends BaseAdapter {

    private TextView commentnick;
    private TextView com_created_at;
    private TextView comment;
    private TextView delComment;
    private TextView upComment;
    private TextView com_cnt;

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<CommentReq> commentReqArrayList;

    CommentReq commentReq;
    int question_id;


    public QCommentAdapter(Context mContext, ArrayList<CommentReq> commentReqArrayList) {
        this.mContext = mContext;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.commentReqArrayList = commentReqArrayList;
    }


    @Override
    public int getCount() {
        return commentReqArrayList.size();
    }

    @Override
    public CommentReq getItem(int position) {
        return commentReqArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        view = mLayoutInflater.inflate(R.layout.comment_row, null);
        SharedPreferences sp = mContext.getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
        String token = sp.getString("token", null);
        int index = position;
        commentReq = commentReqArrayList.get(index);
        question_id = commentReq.getQuestion_id();
        commentnick = view.findViewById(R.id.commentnick);
        com_created_at = view.findViewById(R.id.com_created_at);
        comment = view.findViewById(R.id.comment);

        com_cnt = view.findViewById(R.id.com_cnt);

        delComment = view.findViewById(R.id.delComment);
        upComment = view.findViewById(R.id.upComment);

        String new_date = "";
        commentnick.setText(commentReq.getNickname());
        String dateTime = commentReq.getCreated_at();
        SimpleDateFormat old_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        old_format.setTimeZone(TimeZone.getTimeZone("KST"));
        SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date old_date = old_format.parse(dateTime);
            new_date = new_format.format(old_date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        com_created_at.setText("" + new_date);
        comment.setText(commentReq.getComment());

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        String login_email = sharedPreferences.getString("email", null);
        String email = commentReq.getEmail();

        if(login_email != null){
            if (login_email.equals(email)) {
                delComment.setVisibility(View.VISIBLE);
                upComment.setVisibility(View.VISIBLE);
            } else {

            }
        }


        delComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("댓글 삭제");
                builder.setMessage("댓글을 삭제하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        int index = position;
                        question_id = commentReq.getQuestion_id();
                        CommentReq delReq = commentReqArrayList.get(index);
                        int cmt_no = delReq.getCmt_no();
                        CommentReq id = new CommentReq(cmt_no, question_id, 0, 0);

                        Retrofit retrofit = NetworkClient.getRetrofitClient(mContext);
                        CommentApi commentApi = retrofit.create(CommentApi.class);

                        SharedPreferences sp = mContext.getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
                        String token = sp.getString("token", null);

                        Call<CommentRes> call = commentApi.deleteqComment("Bearer " + token, id);
                        call.enqueue(new Callback<CommentRes>() {
                            @Override
                            public void onResponse(Call<CommentRes> call, Response<CommentRes> response) {

                                ((QuestionDetail) QuestionDetail.context).minusCnt(response.body().getCnt());
                                ((QuestionDetail) QuestionDetail.context).onResume();
                            }

                            @Override
                            public void onFailure(Call<CommentRes> call, Throwable t) {

                            }
                        });

                    }
                });
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                builder.setCancelable(true);
                            }
                        });
                builder.show();
            }
        });


        upComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("댓글 수정");
                final EditText uptxt = new EditText(mContext);
                builder.setView(uptxt);


                int index = position;
                CommentReq upReq = commentReqArrayList.get(index);
                String before_comment = upReq.getComment();
                int cmt_no = upReq.getCmt_no();
                uptxt.setText(before_comment);
                builder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String comment = uptxt.getText().toString().trim();
                                CommentReq commentReq = new CommentReq(cmt_no, question_id, comment, 0);

                                Retrofit retrofit = NetworkClient.getRetrofitClient(mContext);
                                CommentApi commentApi = retrofit.create(CommentApi.class);

                                SharedPreferences sp = mContext.getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                                String token = sp.getString("token", null);

                                Call<CommentRes> call = commentApi.updateqComment("Bearer " + token, commentReq);
                                call.enqueue(new Callback<CommentRes>() {
                                    @Override
                                    public void onResponse(Call<CommentRes> call, Response<CommentRes> response) {
                                        Log.i("AAA", "" + response.body().getSuccess().toString());
                                        ((QuestionDetail) QuestionDetail.context).onResume();
                                    }

                                    @Override
                                    public void onFailure(Call<CommentRes> call, Throwable t) {

                                    }
                                });
                                dialog.dismiss();
                            }
                        });
                builder.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                builder.setCancelable(true);
                                dialog.dismiss();
                            }
                        });

                builder.show();
            }
        });
        return view;
    }
}

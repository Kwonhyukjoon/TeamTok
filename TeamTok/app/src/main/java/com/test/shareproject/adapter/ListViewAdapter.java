package com.test.shareproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.shareproject.Board.DetailActivity;
import com.test.shareproject.Question.QuestionDetail;
import com.test.shareproject.R;
import com.test.shareproject.Util.Utils;
import com.test.shareproject.api.BoardApi;
import com.test.shareproject.api.NetworkClient;
import com.test.shareproject.api.QuestionApi;
import com.test.shareproject.model.BoardReq;
import com.test.shareproject.model.Res;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListViewAdapter extends BaseAdapter {

    private TextView txtCategory;
    private TextView txtTitle;
    private TextView txtNickname;
    private TextView Period;
    private LinearLayout List;
    private TextView com_cnt;

    Activity mActivity = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<BoardReq> boardArraylist;
    BoardReq boardReq;

    SharedPreferences sp;
    String token;

    public ListViewAdapter(Activity mActivity, ArrayList<BoardReq> boardArraylist) {
        this.mActivity = mActivity;
        this.mLayoutInflater = LayoutInflater.from(mActivity);
        this.boardArraylist = boardArraylist;
    }

    @Override
    public int getCount() {
        return boardArraylist.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public BoardReq getItem(int position) {
        return boardArraylist.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = mLayoutInflater.inflate(R.layout.listview_row, null);
        sp = mActivity.getSharedPreferences(Utils.PREFERENCES_NAME, Context.MODE_PRIVATE);
        token = sp.getString("token", null);
        int index = position;
        boardReq = boardArraylist.get(index);

        // 공모전 게시판
        txtCategory = view.findViewById(R.id.txtCategory);
        txtTitle = view.findViewById(R.id.txtTitle);
        txtNickname = view.findViewById(R.id.txtNickname);
        Period = view.findViewById(R.id.Period);
        com_cnt = view.findViewById(R.id.com_cnt);

        String new_date = "";
        String new_start_time = "";
        String new_end_time = "";
        txtCategory.setText(" [ " + boardReq.getCategory() + " ] ");
        txtCategory.setPaintFlags(txtCategory.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        txtTitle.setText(boardReq.getTitle());

        String dateTime = boardReq.getCreated_at();
        String startTime = boardReq.getStarttime();
        String endTime = boardReq.getEndtime();
        SimpleDateFormat old_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        old_format.setTimeZone(TimeZone.getTimeZone("KST"));
        SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (endTime != null) {
                Date old_date = old_format.parse(dateTime);
                new_date = new_format.format(old_date);
                Date start_date = old_format.parse(startTime);
                new_start_time = new_format.format(start_date);
                Date end_date = old_format.parse(endTime);
                new_end_time = new_format.format(end_date);
            } else if (endTime == null) {
                Date old_date = old_format.parse(dateTime);
                new_date = new_format.format(old_date);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (endTime == null) {
            txtNickname.setText(boardReq.getNickname() + " · " + new_date + " · " + "조회 " + boardReq.getView_cnt());
            Period.setVisibility(View.GONE);
        } else {
            if(new_start_time.equals(new_end_time)){
                Period.setText(" 기간 : " + new_start_time + " (하루만) ");
            }else{
                Period.setText(" 기간 : " + new_start_time + " ~ " + new_end_time);
            }
            txtNickname.setText(" "+boardReq.getNickname() + " · " + new_date + " · " + "조회 " + boardReq.getView_cnt());
        }
        com_cnt.setText(" " + boardReq.getCom_cnt());


        List = view.findViewById(R.id.List);
        List.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = position;
                boardReq = boardArraylist.get(index);
                int board_id = boardReq.getBoardId();
                int question_id = boardReq.getQuestionId();
                if (token != null) {
                    if (board_id != -1) {
                        BoardReq id = new BoardReq(board_id);

                        Retrofit retrofit = NetworkClient.getRetrofitClient(mActivity);
                        BoardApi boardApi = retrofit.create(BoardApi.class);

                        Call<Res> call = boardApi.viewBoard("Bearer " + token, id);
                        call.enqueue(new Callback<Res>() {
                            @Override
                            public void onResponse(Call<Res> call, Response<Res> response) {
                                Log.i("AAA" , "!@#@#" + response.body().getData().get(0).getStarttime());
                                int is_favorite = response.body().getData().get(0).getIs_favorite();
                                Intent detailintent = new Intent(mActivity, DetailActivity.class);
                                detailintent.putExtra("detail", boardReq);
                                detailintent.putExtra("is_favorite", is_favorite);
                                mActivity.startActivity(detailintent);
                            }

                            @Override
                            public void onFailure(Call<Res> call, Throwable t) {

                            }
                        });
                    } else {
                        //질문
                        BoardReq id = new BoardReq(question_id, 0);
                        Retrofit retrofit = NetworkClient.getRetrofitClient(mActivity);
                        QuestionApi questionApi = retrofit.create(QuestionApi.class);

                        Call<Res> call = questionApi.viewQuestion("Bearer " + token, id);
                        call.enqueue(new Callback<Res>() {
                            @Override
                            public void onResponse(Call<Res> call, Response<Res> response) {
                                int is_favorite = response.body().getItems().get(0).getIs_favorite();
                                Intent detailintent = new Intent(mActivity, QuestionDetail.class);
                                detailintent.putExtra("detail", boardReq);
                                detailintent.putExtra("is_favorite", is_favorite);
                                mActivity.startActivity(detailintent);
                            }

                            @Override
                            public void onFailure(Call<Res> call, Throwable t) {
                                Log.i("AAA", "!#@!@#@#" + t.toString());
                            }
                        });

                    }
                } else {
                    if (board_id != -1) {
                        BoardReq id = new BoardReq(board_id);

                        Retrofit retrofit = NetworkClient.getRetrofitClient(mActivity);
                        BoardApi boardApi = retrofit.create(BoardApi.class);

                        Call<Res> call = boardApi.nomemeberBoard(id);
                        call.enqueue(new Callback<Res>() {
                            @Override
                            public void onResponse(Call<Res> call, Response<Res> response) {
                                Intent detailintent = new Intent(mActivity, DetailActivity.class);
                                detailintent.putExtra("detail", boardReq);
                                mActivity.startActivity(detailintent);
                            }

                            @Override
                            public void onFailure(Call<Res> call, Throwable t) {

                            }
                        });
                    } else {
                        // 질문
                        BoardReq id = new BoardReq(question_id, 0);
                        Retrofit retrofit = NetworkClient.getRetrofitClient(mActivity);
                        QuestionApi questionApi = retrofit.create(QuestionApi.class);

                        Call<Res> call = questionApi.qmemberBoard(id);
                        call.enqueue(new Callback<Res>() {
                            @Override
                            public void onResponse(Call<Res> call, Response<Res> response) {
                                Intent detailintent = new Intent(mActivity, QuestionDetail.class);
                                detailintent.putExtra("detail", boardReq);
                                mActivity.startActivity(detailintent);
                            }

                            @Override
                            public void onFailure(Call<Res> call, Throwable t) {

                            }
                        });
                    }
                }
            }
        });


        return view;


    }

}



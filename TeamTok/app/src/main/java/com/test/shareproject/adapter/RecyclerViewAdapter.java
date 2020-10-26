package com.test.shareproject.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;

    BoardReq boardReq;
    ArrayList<BoardReq> boardReqArrayList;

    SharedPreferences sp;
    String token;

    long l_today;
    Long d_day;

    // 생성자 만들기
    public RecyclerViewAdapter(Context context, ArrayList<BoardReq> boardReqArrayList) {

        this.context = context;
        this.boardReqArrayList = boardReqArrayList;
    }

    // 화면 구성
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false);
        sp = context.getSharedPreferences(Utils.PREFERENCES_NAME, Context.MODE_PRIVATE);
        token = sp.getString("token", null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        boardReq = boardReqArrayList.get(position);

        String category = boardReq.getCategory();
        String title = boardReq.getTitle();
        String nickname = boardReq.getNickname();
        String endTime = boardReq.getEndtime();

        Calendar today = Calendar.getInstance();

        l_today = today.getTimeInMillis() / (24*60*60*1000);
        SimpleDateFormat old_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        old_format.setTimeZone(TimeZone.getTimeZone("KST"));
        SimpleDateFormat new_format = new SimpleDateFormat("MM/dd");


        String new_end_time = "";
        try {
            if (endTime != null) {
                Date end_date = old_format.parse(endTime);
                new_end_time = new_format.format(end_date);
                Date end_date_long = old_format.parse(endTime,new ParsePosition(0));
                d_day = end_date_long.getTime() / (24*60*60*1000);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (endTime != null) {
            long substract = l_today - d_day;
            if(substract == 0 ){
                holder.Period.setTextColor(Color.RED);
                holder.Period.setText("(" + "D-Day" + ")");
            }else if(substract > 0){
                holder.Period.setTextColor(Color.RED);
                holder.Period.setText("마감완료");
            }else if(substract < 0){
                holder.Period.setTextColor(Color.BLUE);
                holder.Period.setText("" + new_end_time + "(D" + substract + ")");
            }
            holder.txtCategory.setText(" [ " + category + " ] ");
            holder.txtTitle.setText(title);
            holder.txtNick.setText(nickname);
        } else {
            holder.Period.setVisibility(View.GONE);
            holder.txtCategory.setText(" [ " + category + " ] ");
            holder.txtTitle.setText(title);
            holder.txtNick.setText(nickname);

        }

    }

    // 리스트에 있는 데이터의 갯수를 리턴해줘야 한다.
    @Override
    public int getItemCount() {
        return boardReqArrayList.size();
    }


    // 하나의 셀(템플릿) xml 화면에 있는 구성 요소(텍스트뷰, 이미지뷰 등등)를 여기서 연결한다.
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtCategory;
        public TextView txtTitle;
        public TextView txtNick;
        public TextView Period;


        public CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtNick = itemView.findViewById(R.id.txtNick);
            Period = itemView.findViewById(R.id.Period);

            cardView = itemView.findViewById(R.id.cardView);


            // 카드뷰의 클릭 이벤트 처리
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    boardReq = boardReqArrayList.get(index);

                    int board_id = boardReq.getBoardId();
                    int question_id = boardReq.getQuestionId();

                    if (token != null) {
                        if (board_id != -1) {
                            BoardReq id = new BoardReq(board_id);

                            Retrofit retrofit = NetworkClient.getRetrofitClient(context);
                            BoardApi boardApi = retrofit.create(BoardApi.class);

                            Call<Res> call = boardApi.viewBoard("Bearer " + token, id);
                            call.enqueue(new Callback<Res>() {
                                @Override
                                public void onResponse(Call<Res> call, Response<Res> response) {
                                    int is_favorite = response.body().getData().get(0).getIs_favorite();
                                    Intent detailintent = new Intent(context, DetailActivity.class);
                                    detailintent.putExtra("detail", boardReq);
                                    detailintent.putExtra("is_favorite", is_favorite);
                                    context.startActivity(detailintent);
                                }

                                @Override
                                public void onFailure(Call<Res> call, Throwable t) {

                                }
                            });
                        } else {
                            //질문
                            BoardReq id = new BoardReq(question_id, 0);
                            Retrofit retrofit = NetworkClient.getRetrofitClient(context);
                            QuestionApi questionApi = retrofit.create(QuestionApi.class);

                            Call<Res> call = questionApi.viewQuestion("Bearer " + token, id);
                            call.enqueue(new Callback<Res>() {
                                @Override
                                public void onResponse(Call<Res> call, Response<Res> response) {
                                    int is_favorite = response.body().getItems().get(0).getIs_favorite();
                                    Intent detailintent = new Intent(context, QuestionDetail.class);
                                    detailintent.putExtra("detail", boardReq);
                                    detailintent.putExtra("is_favorite", is_favorite);
                                    context.startActivity(detailintent);
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

                            Retrofit retrofit = NetworkClient.getRetrofitClient(context);
                            BoardApi boardApi = retrofit.create(BoardApi.class);

                            Call<Res> call = boardApi.nomemeberBoard(id);
                            call.enqueue(new Callback<Res>() {
                                @Override
                                public void onResponse(Call<Res> call, Response<Res> response) {
                                    Intent detailintent = new Intent(context, DetailActivity.class);
                                    detailintent.putExtra("detail", boardReq);
                                    context.startActivity(detailintent);
                                }

                                @Override
                                public void onFailure(Call<Res> call, Throwable t) {

                                }
                            });
                        } else {
                            // 질문
                            BoardReq id = new BoardReq(question_id, 0);
                            Retrofit retrofit = NetworkClient.getRetrofitClient(context);
                            QuestionApi questionApi = retrofit.create(QuestionApi.class);

                            Call<Res> call = questionApi.qmemberBoard(id);
                            call.enqueue(new Callback<Res>() {
                                @Override
                                public void onResponse(Call<Res> call, Response<Res> response) {
                                    Intent detailintent = new Intent(context, QuestionDetail.class);
                                    detailintent.putExtra("detail", boardReq);
                                    context.startActivity(detailintent);
                                }

                                @Override
                                public void onFailure(Call<Res> call, Throwable t) {

                                }
                            });
                        }
                    }
                }
            });


        }
    }
}
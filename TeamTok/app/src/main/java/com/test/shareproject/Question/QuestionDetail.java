package com.test.shareproject.Question;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.test.shareproject.Board.DetailActivity;
import com.test.shareproject.Board.UpdateActivity;
import com.test.shareproject.R;
import com.test.shareproject.Util.Utils;

import com.test.shareproject.adapter.CommentAdapter;
import com.test.shareproject.adapter.QCommentAdapter;
import com.test.shareproject.api.BoardApi;
import com.test.shareproject.api.CommentApi;
import com.test.shareproject.api.FavoritesApi;
import com.test.shareproject.api.NetworkClient;
import com.test.shareproject.api.QuestionApi;

import com.test.shareproject.model.BoardReq;
import com.test.shareproject.model.CommentReq;
import com.test.shareproject.model.CommentRes;
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

public class QuestionDetail extends AppCompatActivity {

    public static Context context;

    TextView detailCategory;
    TextView detailTitle;
    TextView detailEtc;
    TextView detailContent;
    EditText txtComment;
    TextView com_cnt;
    TextView txtfav;

    Button btnComment;

    BoardReq boardReq;

    int question_id;
    String token;
    SharedPreferences sp;
    int cmt_no;
    int is_favorite;

    ListView listView;
    ArrayList<CommentReq> commentReqArrayList = new ArrayList<>();
    QCommentAdapter adapter;

    LinearLayout Favorite;
    ImageView favImg;

    Toolbar toolbar;

    String login_email;
    String email;

    LinearLayout empty;

    LinearLayout revert;
    ScrollView scroll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김


        detailCategory = findViewById(R.id.detailCategory);
        detailTitle = findViewById(R.id.detailTitle);
        detailEtc = findViewById(R.id.detailEtc);
        detailContent = findViewById(R.id.detailContent);
        listView = findViewById(R.id.listView);
        empty = findViewById(R.id.empty);

        listView.setEmptyView(empty);

        scroll = findViewById(R.id.scroll);

        Favorite = findViewById(R.id.Favorite);
        txtfav = findViewById(R.id.txtfav);
        favImg = findViewById(R.id.favImg);
        com_cnt = findViewById(R.id.com_cnt);

        sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        token = sp.getString("token", null);
        login_email = sp.getString("email", null);


        boardReq = (BoardReq) getIntent().getSerializableExtra("detail");
        String category = boardReq.getCategory();
        String title = boardReq.getTitle();
        String nickname = boardReq.getNickname();
        String content = boardReq.getContent();
        int cnt = boardReq.getCom_cnt();
        question_id = boardReq.getQuestionId();
        email = boardReq.getEmail();

        String new_date = "";
        detailCategory.setText("[ " + category + " ] ");
        detailTitle.setText(title);
        detailContent.setText(content);
        com_cnt.setText("" + cnt);
        String created_at = boardReq.getCreated_at();
        SimpleDateFormat old_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        old_format.setTimeZone(TimeZone.getTimeZone("KST"));
        SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date old_date = old_format.parse(created_at);
            new_date = new_format.format(old_date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        detailEtc.setText(nickname + " | " + new_date);


        is_favorite = getIntent().getIntExtra("is_favorite", -1);
        if (is_favorite == 1) {
            favImg.setImageResource(R.drawable.ic_baseline_favorite_24);
            txtfav.setText("좋아요");
        } else if (is_favorite == 0) {
            favImg.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            txtfav.setText("좋아요");

        }

        if(question_id != -1) {
            Favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(QuestionDetail.this, "누르는 중", Toast.LENGTH_SHORT).show();
                    is_favorite = 1 - is_favorite;

                    if (is_favorite == 0) {
                        BoardReq id = new BoardReq(question_id , 0);
                        Retrofit retrofit = NetworkClient.getRetrofitClient(QuestionDetail.this);
                        FavoritesApi favoritesApi = retrofit.create(FavoritesApi.class);

                        SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, Context.MODE_PRIVATE);
                        String token = sp.getString("token", null);

                        Call<Res> call = favoritesApi.deleteFavorite("Bearer " + token, id);
                        call.enqueue(new Callback<Res>() {
                            @Override
                            public void onResponse(Call<Res> call, Response<Res> response) {
                                favImg.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                                Log.i("AAA", "좋아요 : " + is_favorite);
                                txtfav.setText("좋아요");
                            }

                            @Override
                            public void onFailure(Call<Res> call, Throwable t) {

                            }
                        });

                    } else if (is_favorite == 1) {
                        Log.i("AAA", "board_id : " + question_id);
                        BoardReq id = new BoardReq(question_id,0);
                        Retrofit retrofit = NetworkClient.getRetrofitClient(QuestionDetail.this);
                        FavoritesApi favoritesApi = retrofit.create(FavoritesApi.class);

                        SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, Context.MODE_PRIVATE);
                        String token = sp.getString("token", null);

                        Call<Res> call = favoritesApi.addFavorite("Bearer " + token, id);
                        call.enqueue(new Callback<Res>() {
                            @Override
                            public void onResponse(Call<Res> call, Response<Res> response) {
                                favImg.setImageResource(R.drawable.ic_baseline_favorite_24);
                                Log.i("AAA", "좋아요 좋아요 : " + is_favorite);
                                txtfav.setText("좋아요");
                            }

                            @Override
                            public void onFailure(Call<Res> call, Throwable t) {

                            }
                        });

                    }

                }
            });
        }
        getCommentlist();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String new_date = "";
        if (requestCode == 0 && resultCode == RESULT_OK) {
            boardReq = (BoardReq) data.getSerializableExtra("update");
            detailCategory.setText(" [ " + boardReq.getCategory() + " ] ");
            detailTitle.setText(boardReq.getTitle());
            detailContent.setText(boardReq.getContent());
            String created_at = boardReq.getCreated_at();
            SimpleDateFormat old_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            old_format.setTimeZone(TimeZone.getTimeZone("KST"));
            SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date old_date = old_format.parse(created_at);
                new_date = new_format.format(old_date);
                Log.i("TTT" , ""+new_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            detailEtc.setText(boardReq.getNickname() + " | " + new_date);
        }
    }

    private void getCommentlist() {
        context = this;
        // 댓글리스트 불러오는 API
        CommentReq commentReq = new CommentReq(question_id , 0 , 0);
        Retrofit retrofit = NetworkClient.getRetrofitClient(QuestionDetail.this);
        CommentApi commentApi = retrofit.create(CommentApi.class);
        Call<CommentRes> call = commentApi.getquCommentlist(commentReq);

        call.enqueue(new Callback<CommentRes>() {
            @Override
            public void onResponse(Call<CommentRes> call, Response<CommentRes> response) {
                commentReqArrayList = response.body().getItems();
                adapter = new QCommentAdapter(QuestionDetail.this, commentReqArrayList);
                adapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(listView);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<CommentRes> call, Throwable t) {
                Log.i("CCC", "" + t.toString());
            }
        });

    }

    private void addCommentdata() {
        btnComment = findViewById(R.id.btnComment);
        txtComment = findViewById(R.id.txtComment);
        if (token == null) {
            txtComment.setEnabled(false);
            btnComment.setEnabled(false);
        }
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = txtComment.getText().toString().trim();
                CommentReq addReq = new CommentReq(cmt_no, question_id, comment , null);
                Retrofit retrofit = NetworkClient.getRetrofitClient(QuestionDetail.this);
                CommentApi commentApi = retrofit.create(CommentApi.class);

                SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                String token = sp.getString("token", null);

                if(token == null){
                    Toast.makeText(QuestionDetail.this , "로그인을 해주세요." , Toast.LENGTH_SHORT).show();
                    txtComment.setText("");
                    return;
                }

                Call<CommentRes> call = commentApi.addqComment("Bearer " + token, addReq);
                call.enqueue(new Callback<CommentRes>() {
                    @Override
                    public void onResponse(Call<CommentRes> call, Response<CommentRes> response) {
                        scroll.post(new Runnable() {
                            @Override
                            public void run() {
                                scroll.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });
                        txtComment.setText("");
                        commentReqArrayList = response.body().getItems();
                        com_cnt.setText(""+response.body().getCnt());
                        adapter = new QCommentAdapter(QuestionDetail.this, commentReqArrayList);
                        listView.setAdapter(adapter);
                        setListViewHeightBasedOnChildren(listView);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<CommentRes> call, Throwable t) {
                        Log.i("CCC", "" + t.toString());
                    }
                });
            }
        });

    }

    public void comment(){
        revert = findViewById(R.id.revert);
        revert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCommentlist();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        getCommentlist();

        addCommentdata();

        comment();

    }

    public void minusCnt(int cnt) {
        com_cnt.setText(cnt+"");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if (token == null) {
            inflater.inflate(R.menu.menu_board_not, menu);
        }

        if(login_email !=null){
            if (login_email.equals(email)) {
                inflater.inflate(R.menu.menu_board, menu);
            } else {
                inflater.inflate(R.menu.menu_board_not, menu);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.btn_update:
                Intent updateintent = new Intent(QuestionDetail.this, QuestionUpdate.class);
                updateintent.putExtra("update", boardReq);
                startActivityForResult(updateintent,0);
                break;

            case R.id.btn_delete:
                // 게시글 삭제
                if(question_id != -1) {
                    BoardReq id = new BoardReq(question_id, 0);
                            AlertDialog.Builder builder = new AlertDialog.Builder(QuestionDetail.this);
                            builder.setTitle("게시글 삭제");
                            builder.setMessage("삭제하시겠습니까?");
                            builder.setPositiveButton("예",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            Retrofit retrofit = NetworkClient.getRetrofitClient(QuestionDetail.this);
                                            QuestionApi questionApi = retrofit.create(QuestionApi.class);

                                            SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, Context.MODE_PRIVATE);
                                            String token = sp.getString("token", null);
                                            Log.i("AAA", "권혁준 토큰 : " + token);

                                            Call<Res> call = questionApi.deleteQuestion("Bearer " + token, id);
                                            call.enqueue(new Callback<Res>() {
                                                @Override
                                                public void onResponse(Call<Res> call, Response<Res> response) {
                                                    Log.i("AAA" , "@#" +response.toString());
                                                    finish();
                                                }

                                                @Override
                                                public void onFailure(Call<Res> call, Throwable t) {
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
                break;
                }

        return super.onOptionsItemSelected(item);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}

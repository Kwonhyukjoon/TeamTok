package com.test.shareproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.test.shareproject.Dialog.MypageDialog;
import com.test.shareproject.User.MyUpdateActivity;
import com.test.shareproject.Util.Utils;
import com.test.shareproject.adapter.MypageListAdapter;
import com.test.shareproject.api.MypageApi;
import com.test.shareproject.api.NetworkClient;
import com.test.shareproject.model.BoardReq;
import com.test.shareproject.model.Res;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class Mypage extends Fragment {
    View view;

    ListView pageList;
    ListView cmtList;
    ListView likeList;
    MypageListAdapter adapter;

    TextView txtMynik;
    TextView myEmail;
    ImageView MypageSet;

    ArrayList<BoardReq> boardArraylist = new ArrayList<>();

    TabHost tabHost1;

    SharedPreferences sp;
    String token;

    Toolbar toolbar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sp = getActivity().getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
        token = sp.getString("token" , null);
        String nickname = sp.getString("nickname" , null);
        String email = sp.getString("email",null);
        if(token !=null) {
            view = inflater.inflate(R.layout.activity_mypagefragment, container, false);
            pageList = view.findViewById(R.id.pageList);
            cmtList = view.findViewById(R.id.cmtList);
            likeList = view.findViewById(R.id.likeList);

            myEmail = view.findViewById(R.id.myEmail);
            txtMynik = view.findViewById(R.id.txtMynik);
            MypageSet = view.findViewById(R.id.MypageSet);
            myEmail.setText(""+email);
            txtMynik.setText(nickname + "님!");

            MypageSet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MypageDialog mypageDialog = new MypageDialog(getActivity());
                    mypageDialog.setNickDialogListener(new MypageDialog.NickDialogListener() {
                        @Override
                        public void clickbtn(String nickname) {
                            txtMynik.setText(nickname + "님!");
                        }
                    });
                    mypageDialog.setCancelable(true);
                    mypageDialog.show();

                }
            });


            tabHost1 = view.findViewById(R.id.tabHost1);
            tabHost1.setup();


            // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
            TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1") ;
            ts1.setContent(R.id.btnborad) ;
            ts1.setIndicator("내가 쓴 글") ;
            tabHost1.addTab(ts1);


            // 두 번째 Tab. (탭 표시 텍스트:"TAB 2"), (페이지 뷰:"content2")
            TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2") ;
            ts2.setContent(R.id.btnMycmt) ;
            ts2.setIndicator("내가 쓴 댓글") ;
            tabHost1.addTab(ts2) ;

            // 세 번째 Tab. (탭 표시 텍스트:"TAB 3"), (페이지 뷰:"content3")
            TabHost.TabSpec ts3 = tabHost1.newTabSpec("Tab Spec 3") ;
            ts3.setContent(R.id.btnMylike) ;
            ts3.setIndicator("나의 좋아요") ;
            tabHost1.addTab(ts3) ;

        }else if(token == null){
            view = inflater.inflate(R.layout.activity_unloginfragment, container, false);
        }

        return view;

    }

    public void getMyboardData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        MypageApi mypageApi = retrofit.create(MypageApi.class);

        Call<Res> call = mypageApi.myWrite("Bearer " + token,"desc");

        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                boardArraylist = response.body().getItems();
                adapter = new MypageListAdapter(getActivity(), boardArraylist);
                adapter.notifyDataSetChanged();
                pageList.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
            }
        });
    }

    public void getMyCommentData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        MypageApi mypageApi = retrofit.create(MypageApi.class);

        Call<Res> call = mypageApi.getMycomment("Bearer " + token);

        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                boardArraylist = response.body().getItems();
                adapter = new MypageListAdapter(getActivity(), boardArraylist);
                adapter.notifyDataSetChanged();
                cmtList.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
            }
        });
    }

    public void getmylikeBoard() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        MypageApi mypageApi = retrofit.create(MypageApi.class);

        Call<Res> call = mypageApi.mylikeBoard("Bearer " + token);

        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                boardArraylist = response.body().getItems();
                adapter = new MypageListAdapter(getActivity(), boardArraylist);
                adapter.notifyDataSetChanged();
                likeList.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if(token != null){
            getMyboardData();

            getMyCommentData();

            getmylikeBoard();
        }

    }

    public void getNickname(){
        Intent i = new Intent(getActivity(), MyUpdateActivity.class);
        startActivityForResult(i,-1);
    }
}

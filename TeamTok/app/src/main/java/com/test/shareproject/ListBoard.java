package com.test.shareproject;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;


import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import android.widget.TabHost;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.test.shareproject.Board.WriteActivity;

import com.test.shareproject.Util.Utils;
import com.test.shareproject.adapter.ListViewAdapter;
import com.test.shareproject.adapter.RecyclerViewAdapter;
import com.test.shareproject.api.BoardApi;
import com.test.shareproject.api.NetworkClient;
import com.test.shareproject.model.BoardReq;
import com.test.shareproject.model.Res;

import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class ListBoard extends Fragment{

    View view;

    TabHost tabHost1;

    ListView listView;
    ListView listView1;
    ListView listView2;
    ListViewAdapter adapter;

    ArrayList<BoardReq> boardArraylist = new ArrayList<>();


    FloatingActionButton fab;
    Toolbar toolbar;

    SwipeRefreshLayout swipeLayout;
    SwipeRefreshLayout swipeLayout1;
    SwipeRefreshLayout swipeLayout2;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_boardfragment, container, false);
        listView = view.findViewById(R.id.listView);
        listView1 = view.findViewById(R.id.listView1);
        listView2 = view.findViewById(R.id.listView2);

        toolbar = view.findViewById(R.id.toolbar); //툴바설정
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);

        swipeLayout = view.findViewById(R.id.swipeLayout);
        swipeLayout1 = view.findViewById(R.id.swipeLayout1);
        swipeLayout2 = view.findViewById(R.id.swipeLayout2);

        tabHost1 = view.findViewById(R.id.tabHost1);
        tabHost1.setup();

        // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1")
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1");
        ts1.setContent(R.id.tab1);
        ts1.setIndicator("최근 게시글");
        tabHost1.addTab(ts1);

        // 두 번째 Tab. (탭 표시 텍스트:"TAB 2")
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2");
        ts2.setContent(R.id.tab2);
        ts2.setIndicator("공모중인 글");
        tabHost1.addTab(ts2);

        // 세 번째 Tab. (탭 표시 텍스트:"TAB 3")
        TabHost.TabSpec ts3 = tabHost1.newTabSpec("Tab Spec 3");
        ts3.setContent(R.id.tab3);
        ts3.setIndicator("인기 글");
        tabHost1.addTab(ts3);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getActivity().getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                String token = sp.getString("token", null);
                if (token == null) {
                    Toast.makeText(getActivity(), "로그인을 해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Intent i = new Intent(getActivity(), WriteActivity.class);
                    startActivityForResult(i, 0);
                }
            }
        });

        getNetworkData();

        return view;
    }


    public void getNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        BoardApi boardApi = retrofit.create(BoardApi.class);

        Call<Res> call = boardApi.getBoardlist("desc",' ');
        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                boardArraylist = response.body().getItems();
                adapter = new ListViewAdapter(getActivity(), boardArraylist);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }


            @Override
            public void onFailure(Call<Res> call, Throwable t) {

            }
        });
    }

    public void updateNetworkData() {
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getNetworkData();
                        swipeLayout.setRefreshing(false);
                    }
                }, 1000);

            }
        });
    }



    public void deadlineNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        BoardApi boardApi = retrofit.create(BoardApi.class);
        Call<Res> call = boardApi.DeadlineBoard(' ');

        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                boardArraylist = response.body().getItems();
                adapter = new ListViewAdapter(getActivity(), boardArraylist);
                adapter.notifyDataSetChanged();
                listView1.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                Log.i("AAA", "!!!!!!!!!" + t.toString());
            }
        });
    }
    public void updatedeadlineNetworkData() {
        swipeLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        deadlineNetworkData();
                        swipeLayout1.setRefreshing(false);
                    }
                }, 1000);

            }
        });
    }

    public void topNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        BoardApi boardApi = retrofit.create(BoardApi.class);
        Call<Res> call = boardApi.topboard("desc",' ');

        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                boardArraylist = response.body().getItems();
                adapter = new ListViewAdapter(getActivity(), boardArraylist);
                adapter.notifyDataSetChanged();
                listView2.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                Log.i("AAA", "!!!!!!!!!" + t.toString());
            }
        });
    }

    public void updatetopNetworkData() {
        swipeLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        deadlineNetworkData();
                        swipeLayout2.setRefreshing(false);
                    }
                }, 1000);

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        getNetworkData();
        updateNetworkData();

        deadlineNetworkData();
        updatedeadlineNetworkData();


        topNetworkData();
        updatetopNetworkData();


    }

}

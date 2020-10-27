package com.test.shareproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.shareproject.Util.Utils;
import com.test.shareproject.adapter.ListViewAdapter;
import com.test.shareproject.adapter.RecyclerViewAdapter;
import com.test.shareproject.api.BoardApi;
import com.test.shareproject.api.NetworkClient;
import com.test.shareproject.api.QuestionApi;
import com.test.shareproject.model.BoardReq;
import com.test.shareproject.model.Res;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Home extends Fragment {

    View view;

    TextView joinNick;

    RecyclerView recyclerView;
    RecyclerView recyclerView1;
    RecyclerView recyclerView2;
    RecyclerView recyclerView3;
    RecyclerView recyclerView4;
    RecyclerViewAdapter recyclerViewAdapter;

    ArrayList<BoardReq> boardArraylist = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_homefragment, container, false);

        joinNick = view.findViewById(R.id.joinNick);
        SharedPreferences sp = getActivity().getSharedPreferences(Utils.PREFERENCES_NAME, Context.MODE_PRIVATE);
        String nickname = sp.getString("nickname", null);
        String token = sp.getString("token" , null);
        Log.i("AAA" , "!@#@#" + token);
        if (token == null) {
            joinNick.setText("비회원 님!");
        } else {
            joinNick.setText(nickname + " 님!");
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        recyclerView1 = view.findViewById(R.id.recyclerView1);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView2 = view.findViewById(R.id.recyclerView2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView3 = view.findViewById(R.id.recyclerView3);
        recyclerView3.setHasFixedSize(true);
        recyclerView3.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView4 = view.findViewById(R.id.recyclerView4);
        recyclerView4.setHasFixedSize(true);
        recyclerView4.setLayoutManager(new LinearLayoutManager(getActivity()));


        return view;

    }

    public void getBoardNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        BoardApi boardApi = retrofit.create(BoardApi.class);
        Call<Res> call = boardApi.getBoardlist("desc",5);

        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                boardArraylist = response.body().getItems();
                recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), boardArraylist);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                Log.i("AAA", "!!!!!!!!!" + t.toString());
            }
        });
    }

    public void deadlineNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        BoardApi boardApi = retrofit.create(BoardApi.class);
        Call<Res> call = boardApi.DeadlineBoard( 5);

        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                boardArraylist = response.body().getItems();
                recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), boardArraylist);
                recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recyclerView1.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
            }
        });
    }

    public void topNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        BoardApi boardApi = retrofit.create(BoardApi.class);
        Call<Res> call = boardApi.topboard("desc",5);

        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                boardArraylist = response.body().getItems();
                recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), boardArraylist);
                recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recyclerView2.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                Log.i("AAA", "!!!!!!!!!" + t.toString());
            }
        });
    }

    public void getquestionNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        QuestionApi questionApi = retrofit.create(QuestionApi.class);
        Call<Res> call = questionApi.latestQuestion(5);

        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                boardArraylist = response.body().getItems();
                recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), boardArraylist);
                recyclerView3.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recyclerView3.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {

            }
        });
    }

    public void getqtopBoard() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        QuestionApi questionApi = retrofit.create(QuestionApi.class);
        Call<Res> call = questionApi.qtopBoard("desc" , 5);

        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                boardArraylist = response.body().getItems();
                recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), boardArraylist);
                recyclerView4.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                recyclerView4.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        getBoardNetworkData();

        deadlineNetworkData();

        topNetworkData();

        getquestionNetworkData();

        getqtopBoard();

    }
}
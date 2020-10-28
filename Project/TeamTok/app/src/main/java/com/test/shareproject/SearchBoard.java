package com.test.shareproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;


import com.test.shareproject.Board.DetailActivity;
import com.test.shareproject.adapter.ListViewAdapter;
import com.test.shareproject.api.BoardApi;
import com.test.shareproject.api.NetworkClient;
import com.test.shareproject.model.BoardReq;
import com.test.shareproject.model.Res;

import java.net.URLEncoder;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchBoard extends Fragment {
    View view;

    Toolbar toolbar;

    ListView listView;
    ListViewAdapter adapter;

    Spinner Category;
    ArrayAdapter arrayAdapter; // spinner 셋팅용
    ImageView search;
    EditText txtsearch;

    String TAG = "CCC";
    String category; // spinnercategory

    ArrayList<BoardReq> boardArraylist = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_search_board,container,false);
        Category = view.findViewById(R.id.Category);
        arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_array, android.R.layout.simple_spinner_dropdown_item);
        Category.setAdapter(arrayAdapter);
        listView = view.findViewById(R.id.listView);

        setHasOptionsMenu(true);
        toolbar = view.findViewById(R.id.toolbar); //툴바설정
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);

        // Spinner 항목 클릭시 설정
        Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int categoryType, long l) {
                switch (categoryType) {
                    case 0:
                        Log.i(TAG, Integer.toString(categoryType));
                        category = null;
                        break;
                    case 1:
                        Log.i(TAG, Integer.toString(categoryType));
                        category = "Android";
                        break;
                    case 2:
                        Log.i(TAG, Integer.toString(categoryType));
                        category = "ios";
                        break;
                    case 3:
                        Log.i(TAG, Integer.toString(categoryType));
                        category = "Game";
                        break;
                    case 4:
                        Log.i(TAG, Integer.toString(categoryType));
                        category = "Web";
                        break;
                    case 5:
                        Log.i(TAG, Integer.toString(categoryType));
                        category = "Server";
                        break;
                    case 6:
                        Log.i(TAG, Integer.toString(categoryType));
                        category = "기타";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;
    }

    public void SearchData(){
        // 이미지 클릭시
        txtsearch = view.findViewById(R.id.txtsearch);
        search = view.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editsearch = txtsearch.getText().toString().trim();


                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txtsearch.getWindowToken(),0);
                txtsearch.setText("");

                Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
                BoardApi boardApi = retrofit.create(BoardApi.class);
                Call<Res> call = boardApi.searchBoard(category,editsearch);
                call.enqueue(new Callback<Res>() {
                    @Override
                    public void onResponse(Call<Res> call, Response<Res> response) {
                        try {
                            boardArraylist = response.body().getItems();
                            adapter = new ListViewAdapter(getActivity(), boardArraylist);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);


                        } catch (NullPointerException e) {
                            Log.i("CCC", "" + e.toString());
                        }

                    }

                    @Override
                    public void onFailure(Call<Res> call, Throwable t) {
                        Log.i("CCC", t.toString());
                    }
                });

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        SearchData();
    }
}
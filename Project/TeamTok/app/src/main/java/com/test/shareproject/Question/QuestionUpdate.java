package com.test.shareproject.Question;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.test.shareproject.R;
import com.test.shareproject.Util.Utils;

import com.test.shareproject.api.NetworkClient;
import com.test.shareproject.api.QuestionApi;
import com.test.shareproject.model.BoardReq;
import com.test.shareproject.model.Res;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class QuestionUpdate extends AppCompatActivity {

    EditText updateTitle;
    EditText updateContent;

    Spinner Category;
    ArrayAdapter arrayAdapter; // spinner 셋팅용
    String category; // spinnercategory
    String TAG = "CCC";

    BoardReq boardReq;
    ArrayList<BoardReq> boardReqArrayList = new ArrayList<>();

    Toolbar toolbar;

    int question_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_update);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김


        Category = findViewById(R.id.Category);
        arrayAdapter = ArrayAdapter.createFromResource(QuestionUpdate.this, R.array.spinner_array, android.R.layout.simple_spinner_dropdown_item);
        Category.setAdapter(arrayAdapter);

        updateTitle = findViewById(R.id.updateTitle);
        updateContent = findViewById(R.id.updateContent);
        boardReq = (BoardReq) getIntent().getSerializableExtra("update");

        question_id = boardReq.getQuestion_id();
        String categoryname = boardReq.getCategory();
        String title = boardReq.getTitle();
        String content = boardReq.getContent();

        updateTitle.setText("" + title);
        updateContent.setText("" + content);

        setCategorySpinner(categoryname);



        Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int categoryType, long l) {
                Object object = adapterView.getItemAtPosition(categoryType);
                switch (categoryType) {
                    case 0:
                        Log.i(TAG, Integer.toString(categoryType));
                        category = categoryname;
                        Log.i("DDD" , ""+categoryname);
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



    }

    private void setCategorySpinner(String inputCategory) {
        String[] arrSpinner = getResources().getStringArray(R.array.spinner_array);

        for (int i=0; i<7; i++) {
            if (inputCategory.equals(arrSpinner[i])) {
                Category.setSelection(i);
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.btn_complete:
                String title = updateTitle.getText().toString().trim();
                String content = updateContent.getText().toString().trim();

                BoardReq boardReq = new BoardReq(question_id, category, title, content);

                Retrofit retrofit = NetworkClient.getRetrofitClient(QuestionUpdate.this);
                QuestionApi questionApi = retrofit.create(QuestionApi.class);

                SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                String token = sp.getString("token", null);
                Log.i("AAA", "" + token);

                Call<Res> call = questionApi.updateQuestion("Bearer " + token, boardReq);
                call.enqueue(new Callback<Res>() {
                    @Override
                    public void onResponse(Call<Res> call, Response<Res> response) {
                        Log.i("OOO", "" + response.body().toString());
                        boardReqArrayList = response.body().getItems();
                        BoardReq quesionupdate = boardReqArrayList.get(0);
                        Intent i = getIntent();
                        i.putExtra("update", quesionupdate);
                        setResult(RESULT_OK, i);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Res> call, Throwable t) {
                        Log.i("AAA", "" + t.toString());
                    }
                });
                break;
        }

                return super.onOptionsItemSelected(item);
    }
}
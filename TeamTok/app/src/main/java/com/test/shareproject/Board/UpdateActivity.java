package com.test.shareproject.Board;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.test.shareproject.R;
import com.test.shareproject.Util.Utils;
import com.test.shareproject.api.BoardApi;
import com.test.shareproject.api.NetworkClient;
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

public class UpdateActivity extends AppCompatActivity {

    EditText updateTitle;
    EditText updateContent;
    TextView updateSDate;
    TextView updateEDate;
    Button btnDate;

    Spinner Category;
    ArrayAdapter arrayAdapter; // spinner 셋팅용
    String category; // spinnercategory
    String TAG = "CCC";

    BoardReq boardReq;
    ArrayList<BoardReq> boardArraylist = new ArrayList<>();

    String starttime;
    String endtime;
    String new_starttime = "";
    String new_endtime = "";

    int board_id;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        Category = findViewById(R.id.Category);
        arrayAdapter = ArrayAdapter.createFromResource(UpdateActivity.this, R.array.spinner_array, android.R.layout.simple_spinner_dropdown_item);
        Category.setAdapter(arrayAdapter);

        updateTitle = findViewById(R.id.updateTitle);
        updateContent = findViewById(R.id.updateContent);
        updateSDate = findViewById(R.id.updateSDate);
        updateEDate = findViewById(R.id.updateEDate);
        boardReq = (BoardReq) getIntent().getSerializableExtra("update");

        board_id = boardReq.getBoard_id();
        String categoryname = boardReq.getCategory();
        String title = boardReq.getTitle();
        starttime = boardReq.getStarttime();
        endtime = boardReq.getEndtime();
        String content = boardReq.getContent();

        SimpleDateFormat old_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        old_format.setTimeZone(TimeZone.getTimeZone("KST"));
        SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date old_date = old_format.parse(starttime);
            Date old_end = old_format.parse(endtime);
            new_starttime = new_format.format(old_date);
            new_endtime = new_format.format(old_end);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        updateTitle.setText("" + title);
        updateContent.setText("" + content);
        updateSDate.setText("" + new_starttime);
        updateEDate.setText("" + new_endtime);


        setCategorySpinner(categoryname);

        Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int categoryType, long l) {
                Object object = adapterView.getItemAtPosition(categoryType);
                switch (categoryType) {
                    case 0:
                        Log.i(TAG, Integer.toString(categoryType));
                        category = categoryname;
                        Log.i("DDD", "" + categoryname);
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


        btnDate = findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UpdateActivity.this, CalendarActivity.class);
                i.putExtra("start",new_starttime);
                i.putExtra("end" , new_endtime);
                startActivityForResult(i, 0);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            boardReq = (BoardReq) data.getSerializableExtra("Date");
            starttime = boardReq.getStarttime();
            endtime = boardReq.getEndtime();

            Log.i("AAA" , "!@#@#" + starttime + " , " + endtime);

            if (starttime.equals(endtime)) {
                updateSDate.setVisibility(View.GONE);
                updateEDate.setText(""+starttime);
                updateEDate.setTextSize(15);
            } else {
                updateSDate.setVisibility(View.VISIBLE);
                updateSDate.setText(starttime);
                updateEDate.setText(endtime);
            }
        }
    }

    public void setCategorySpinner(String inputCategory) {
        String[] arrSpinner = getResources().getStringArray(R.array.spinner_array);

        for (int i = 0; i < 7; i++) {
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
                String startDate = updateSDate.getText().toString().trim();
                String endDate = updateEDate.getText().toString().trim();

                BoardReq boardReq = new BoardReq(board_id, category, title, content, startDate, endDate);

                Retrofit retrofit = NetworkClient.getRetrofitClient(UpdateActivity.this);
                BoardApi boardApi = retrofit.create(BoardApi.class);

                SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                String token = sp.getString("token", null);

                Call<Res> call = boardApi.updateBoard("Bearer " + token, boardReq);
                call.enqueue(new Callback<Res>() {
                    @Override
                    public void onResponse(Call<Res> call, Response<Res> response) {
                        boardArraylist = response.body().getItems();
                        BoardReq boardupdate = boardArraylist.get(0);
                        Intent i = getIntent();
                        i.putExtra("update", boardupdate);
                        setResult(RESULT_OK, i);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Res> call, Throwable t) {
                        Log.i("AAA", "" + t.toString());
                    }
                });
                break;

            default:
                Toast.makeText(UpdateActivity.this, "선택 안됨", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
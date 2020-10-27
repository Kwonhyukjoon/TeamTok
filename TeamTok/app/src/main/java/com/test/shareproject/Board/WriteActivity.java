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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.test.shareproject.R;
import com.test.shareproject.Util.Utils;
import com.test.shareproject.api.BoardApi;
import com.test.shareproject.api.NetworkClient;
import com.test.shareproject.model.BoardReq;
import com.test.shareproject.model.Res;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class WriteActivity extends AppCompatActivity {

    EditText txtTitle;
    EditText txtContent;
    TextView txtSDate;
    TextView txtEDate;
    TextView btnDate;

    String startDate;
    String endDate;

    Spinner Category;
    ArrayAdapter arrayAdapter; // spinner 셋팅용
    String category; // spinnercategory
    String TAG = "CCC";

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김


        Category = findViewById(R.id.Category);
        arrayAdapter = ArrayAdapter.createFromResource(WriteActivity.this, R.array.spinner_array, android.R.layout.simple_spinner_dropdown_item);
        Category.setAdapter(arrayAdapter);

        txtTitle = findViewById(R.id.txtTitle);
        txtContent = findViewById(R.id.txtContent);
        txtSDate = findViewById(R.id.txtSDate);
        txtEDate = findViewById(R.id.txtEDate);


        btnDate = findViewById(R.id.btnDate);


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

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WriteActivity.this, CalendarActivity.class);
                startActivityForResult(i, 0);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        txtSDate = findViewById(R.id.txtSDate);
        txtEDate = findViewById(R.id.txtEDate);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            BoardReq boardReq = (BoardReq) data.getSerializableExtra("Date");
            startDate = boardReq.getStarttime();
            endDate = boardReq.getEndtime();


            if (startDate.equals(endDate)) {
                txtSDate.setVisibility(View.GONE);
                txtEDate.setText(""+startDate);
                txtEDate.setTextSize(15);
            } else {
                txtSDate.setVisibility(View.VISIBLE);
                txtSDate.setText(startDate);
                txtEDate.setText(endDate);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_update,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.btn_complete:
                String title = txtTitle.getText().toString().trim();
                String content = txtContent.getText().toString().trim();
                String startDate = txtSDate.getText().toString().trim();
                String endDate = txtEDate.getText().toString().trim();

                Log.i("AAA",""+startDate + "," + ""+endDate);

                if (category == null) {
                    Toast.makeText(WriteActivity.this, "카테고리를 골라주세요.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (title == null) {
                    Toast.makeText(WriteActivity.this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (content == null) {
                    Toast.makeText(WriteActivity.this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if(startDate.isEmpty() || endDate.isEmpty()){
                    Toast.makeText(WriteActivity.this, "날짜를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                BoardReq boardReq = new BoardReq(category , title , content , startDate, endDate);

                Retrofit retrofit = NetworkClient.getRetrofitClient(WriteActivity.this);
                BoardApi boardApi = retrofit.create(BoardApi.class);

                SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                String token = sp.getString("token", null);
                String email = sp.getString("email" , null);
                Log.i("HHH", "" + token + " , " + email);

                Call<Res> call = boardApi.BoardUpload("Bearer " + token , boardReq);
                call.enqueue(new Callback<Res>() {
                    @Override
                    public void onResponse(Call<Res> call, Response<Res> response) {
                        if(response.isSuccessful()){
                            Log.i("AAA" , response.body().getData().get(0).getCategory());
                            finish();

                        }
                    }

                    @Override
                    public void onFailure(Call<Res> call, Throwable t) {
                        Log.i("AAA", "error : " + t.toString());
                    }
                });

                break;

            default:
                Toast.makeText(WriteActivity.this, "선택 안됨",Toast.LENGTH_SHORT).show();
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}



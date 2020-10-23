package com.test.shareproject.Board;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.OrientationHelper;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;


import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.test.shareproject.R;
import com.test.shareproject.model.BoardReq;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    CalendarView calendarView;
    ArrayList arrayList;

    String day_full = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_calendar);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김


        initViews();
    }

    private void initViews() {
        calendarView = findViewById(R.id.calendarView);
        calendarView.setCalendarOrientation(OrientationHelper.HORIZONTAL);
        calendarView.setSelectionType(SelectionType.RANGE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.clear_selections:
                clearSelectionsMenuClick();
                return true;


            case R.id.show_selections:
                List<Calendar> days = calendarView.getSelectedDates();
                String startDate = "null";
                String endDate = "null";
                arrayList = new ArrayList<String>();
                for (int i = 0; i < days.size(); i++) {
                    Calendar calendar = days.get(i);
                    final int day = calendar.get(Calendar.DAY_OF_MONTH);
                    final int month = calendar.get(Calendar.MONTH);
                    final int year = calendar.get(Calendar.YEAR);
                    if (day < 10) {
                        if (month + 1 < 10) {
                            day_full = year + "-" + "0" + (month + 1) + "-" + "0" + day;
                        } else if (month + 1 >= 10) {
                            day_full = year + "-" + (month + 1) + "-" + "0" + day;
                        }
                    } else if (day >= 10) {
                        if (month + 1 < 10) {
                            day_full = year + "-" + "0" + (month + 1) + "-" + day;
                        } else if (month + 1 >= 10) {
                            day_full = year + "-" + (month + 1) + "-" + day;
                        }
                    }
                    arrayList.add(day_full);
                    Log.i("AAA", "!@#@#" + day_full);
                }

                if (arrayList.size() == 0) {
                    arrayList.add(0, null);
                    Toast.makeText(CalendarActivity.this, "날짜를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    startDate = (String) arrayList.get(0);
                    int x = arrayList.size() - 1;
                    endDate = (String) arrayList.get(x);
                }

                Intent i = getIntent();
                BoardReq boardReq = new BoardReq(startDate, endDate);
                i.putExtra("Date", boardReq);
                setResult(RESULT_OK, i);
                finish();

                break;

            default:
                Toast.makeText(CalendarActivity.this, "선택 안됨",Toast.LENGTH_SHORT).show();
                break;

        }
                return super.onOptionsItemSelected(item);

    }


    private void clearSelectionsMenuClick() {
        calendarView.clearSelections();
    }

}
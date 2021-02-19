package com.example.bookkeepingdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bookkeepingdemo.adapter.ChartVPAdapter;
import com.example.bookkeepingdemo.db.DBManager;
import com.example.bookkeepingdemo.frag_chart.IncomeChartFragment;
import com.example.bookkeepingdemo.frag_chart.OutcomeChartFragment;
import com.example.bookkeepingdemo.frag_record.IncomeFragment;
import com.example.bookkeepingdemo.frag_record.OutcomeFragment;
import com.example.bookkeepingdemo.utils.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthChartActivity extends AppCompatActivity {

    Button inBtn, outBtn;
    TextView dateTv,inTv,outTv;
    ViewPager chartVp;
    private int year;
    private int month;

    int selectPos = -1;
    int selectMonth = -1;

    List<Fragment> chartFragList;
    private  IncomeChartFragment incomeChartFragment;
    private OutcomeChartFragment outcomeChartFragment;
    private ChartVPAdapter chartVPAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_chart);
        initView();
        initTime();
        initStatistics(year,month);
        initFrag();
        setVPSelectListener();
    }

    private void setVPSelectListener() {

        chartVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                setButtonStyle(position);
            }
        });
    }

    private void initFrag() {
        chartFragList = new ArrayList<>();
        IncomeChartFragment incomeChartFragment = new IncomeChartFragment();
        OutcomeChartFragment outcomeChartFragment = new OutcomeChartFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("year",year);
        bundle.putInt("month",month);
        incomeChartFragment.setArguments(bundle);
        outcomeChartFragment.setArguments(bundle);

        chartFragList.add(outcomeChartFragment);
        chartFragList.add(incomeChartFragment);

        ChartVPAdapter chartVPAdapter = new ChartVPAdapter(getSupportFragmentManager(), chartFragList);
        chartVp.setAdapter(chartVPAdapter);

    }

    private void initStatistics(int year, int month) {

        int inCount = DBManager.getCountItemOneMonth(year, month, 1);
        float inMoney = DBManager.getSumMoneyOneMonth(year,month, 1);

        int outCount = DBManager.getCountItemOneMonth(year,month,0);
        float outMoney = DBManager.getSumMoneyOneMonth(year,month,0);

        dateTv.setText(year+"月"+month+"月账单");
        inTv.setText("共"+inCount+"笔收入，￥"+ inMoney);
        outTv.setText("共"+outCount+"笔支出，￥"+outMoney);


    }

    private void initTime() {

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
    }


    private void initView() {
        inBtn = findViewById(R.id.chart_btn_in);
        outBtn = findViewById(R.id.chart_btn_out);
        dateTv = findViewById(R.id.chart_tv_date);
        inTv = findViewById(R.id.chart_tv_income);
        outTv = findViewById(R.id.chart_tv_outcome);
        chartVp = findViewById(R.id.chart_vp);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chart_iv_back:
                finish();
                break;
            case R.id.chart_iv_calendar:
                showCalendarDialog();
                break;
            case R.id.chart_btn_in:
                setButtonStyle(1);
                chartVp.setCurrentItem(1);
                break;
            case R.id.chart_btn_out:
                setButtonStyle(0);
                chartVp.setCurrentItem(0);
                break;
        }
    }

    private void showCalendarDialog() {
        CalendarDialog dialog = new CalendarDialog(this, selectPos, selectMonth);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
            @Override
            public void onRefresh(int selPos, int year, int month) {
                MonthChartActivity.this.selectPos = selPos;
                MonthChartActivity.this.selectMonth = month;
                initStatistics(year,month);
                incomeChartFragment.setDate(year,month);
                outcomeChartFragment.setDate(year,month);

            }
        });
    }

    private  void setButtonStyle(int kind){
        if (kind == 0) {
            outBtn.setBackgroundResource(R.drawable.main_recordbtn_bg);
            outBtn.setTextColor(Color.WHITE);
            inBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            inBtn.setTextColor(Color.BLACK);
        }else {
            inBtn.setBackgroundResource(R.drawable.main_recordbtn_bg);
            inBtn.setTextColor(Color.WHITE);
            outBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            outBtn.setTextColor(Color.BLACK);
        }
    }
}
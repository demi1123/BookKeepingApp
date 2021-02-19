package com.example.bookkeepingdemo.frag_chart;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bookkeepingdemo.R;
import com.example.bookkeepingdemo.adapter.ChartItemAdapter;
import com.example.bookkeepingdemo.adapter.ChartVPAdapter;
import com.example.bookkeepingdemo.db.BarChartBean;
import com.example.bookkeepingdemo.db.ChartItemBean;
import com.example.bookkeepingdemo.db.DBManager;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;


public class IncomeChartFragment extends BaseChartFragment {

    int kind = 1;

    @Override
    public void onResume() {
        super.onResume();
        loadData(year,month, kind);
    }


    @Override
    protected void setAxisData(int year, int month) {

        List<IBarDataSet> set = new ArrayList<>();
        List<BarChartBean> list = DBManager.getSumMoneyOneDayInMonth(year, month, kind);
        if (list.size()==0) {
            barChart.setVisibility(View.GONE);
            chartTv.setVisibility(View.VISIBLE);
        }else {
            barChart.setVisibility(View.VISIBLE);
            chartTv.setVisibility(View.GONE);

            List<BarEntry> barEntries = new ArrayList<>();
            for (int i = 0; i < 31; i++) {
                // 初始化每一根柱子
                BarEntry barEntry = new BarEntry(i, 0.0f);
                barEntries.add(barEntry);
            }

            for (int i = 0; i < list.size(); i++) {
                BarChartBean barChartBean = list.get(i);
                int day = barChartBean.getDay();
                int xIndex = day-1;
                BarEntry barEntry = barEntries.get(xIndex);
                barEntry.setY(barChartBean.getSumMoney());
            }

            BarDataSet barDataSet = new BarDataSet(barEntries,"");
            barDataSet.setValueTextColor(Color.BLACK);
            barDataSet.setValueTextSize(8f);
            barDataSet.setColor(Color.parseColor("#006400"));

            barDataSet.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    if (value==0) {
                        return "";
                    }
                    return value+"";
                }
            });
            set.add(barDataSet);
            BarData barData = new BarData(set);
            barData.setBarWidth(0.2f);
            barChart.setData(barData);
        }

    }

    @Override
    protected void setYAxis(int year, int month) {
        // 本月收入最高的一天 设为y轴最大值
        float maxMoney = DBManager.getMaxMoneyOneDayInOneMonth(year, month, kind);
        float max = (float) Math.ceil(maxMoney);
        YAxis yAxis_right = barChart.getAxisRight();
        yAxis_right.setAxisMaximum(max);
        yAxis_right.setAxisMinimum(0f);
        yAxis_right.setEnabled(false);

        YAxis yAxis_left = barChart.getAxisLeft();
        yAxis_left.setAxisMaximum(max);
        yAxis_left.setAxisMinimum(0f);
        yAxis_left.setEnabled(false);

        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
    }


    @Override
    public void setDate(int year, int month) {
        super.setDate(year, month);
        loadData(year,month,kind);
    }
}
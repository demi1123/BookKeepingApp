package com.example.bookkeepingdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.bookkeepingdemo.adapter.RecordPagerAdapter;
import com.example.bookkeepingdemo.frag_record.IncomeFragment;
import com.example.bookkeepingdemo.frag_record.BaseRecordFragment;
import com.example.bookkeepingdemo.frag_record.OutcomeFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        tabLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_vp);
        initPaper();
    }

    private void initPaper() {
        List<Fragment> fragmentList = new ArrayList<>();
        OutcomeFragment outcomeFragment = new OutcomeFragment();
        IncomeFragment incomeFragment = new IncomeFragment();
        fragmentList.add(outcomeFragment);
        fragmentList.add(incomeFragment);

        // 创建适配器
        RecordPagerAdapter recordPagerAdapter = new RecordPagerAdapter(getSupportFragmentManager(), fragmentList);
        // 设置适配器
        viewPager.setAdapter(recordPagerAdapter);
        // 将tablayour & viewpager进行关联
        tabLayout.setupWithViewPager(viewPager);

    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_iv_back:
                finish();
                break;

        }
    }
}
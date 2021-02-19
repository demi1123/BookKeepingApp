package com.example.bookkeepingdemo.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.bookkeepingdemo.AboutActivity;
import com.example.bookkeepingdemo.HistoryActivity;
import com.example.bookkeepingdemo.MonthChartActivity;
import com.example.bookkeepingdemo.R;
import com.example.bookkeepingdemo.SettingActivity;

public class MoreDialog extends Dialog implements View.OnClickListener {

    Button aboutBtn, settingBtn, historyBtn, infoBtn;
    ImageView errorIv;

    public MoreDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_more);
        aboutBtn = findViewById(R.id.dialog_more_btn_about);
        settingBtn = findViewById(R.id.dialog_more_btn_setting);
        historyBtn = findViewById(R.id.dialog_more_btn_record);
        infoBtn = findViewById(R.id.dialog_more_btn_info);
        errorIv = findViewById(R.id.dialog_more_iv_close);

        aboutBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        historyBtn.setOnClickListener(this);
        infoBtn.setOnClickListener(this);
        errorIv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.dialog_more_btn_about:
                intent.setClass(getContext(), AboutActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_setting:
                intent.setClass(getContext(), SettingActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_record:
                intent.setClass(getContext(), HistoryActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_info:
                intent.setClass(getContext(), MonthChartActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_iv_close:
                break;
        }
        cancel();
    }

    public void setDialogSize(){

        // 获取当前窗口对象
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        Display defaultDisplay = window.getWindowManager().getDefaultDisplay();
        attributes.width = defaultDisplay.getWidth(); // 对话框窗口为屏幕窗口
        attributes.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(attributes);
    }
}

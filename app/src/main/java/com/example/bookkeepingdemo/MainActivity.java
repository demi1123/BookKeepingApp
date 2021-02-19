package com.example.bookkeepingdemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bookkeepingdemo.adapter.AccountAdapter;
import com.example.bookkeepingdemo.utils.BudgetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.bookkeepingdemo.db.AccountBean;
import com.example.bookkeepingdemo.db.DBManager;
import com.example.bookkeepingdemo.utils.MoreDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ListView todayLv; //展示今日收支情况；
    ImageView searchIv;
    ImageButton moreBtn;
    Button editBtn;

    List<AccountBean> mDatas;
    private  AccountAdapter adapter;

    int year, month, day;

    // 头布局相关
    View headerView;
    TextView topOutTv,topInTv,topBudgetTv, topConTv;
    ImageView topShowIv;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();
        initView();
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);

        // 添加listview的头布局
        addLVHeaderView();
        mDatas = new ArrayList<>();
        // 设置适配器，加载每一行数据到列表当中
        adapter = new AccountAdapter(this, mDatas);
        todayLv.setAdapter(adapter);

    }

    private void initView() {
        todayLv = findViewById(R.id.main_lv);
        moreBtn = findViewById(R.id.main_btn_more);
        editBtn = findViewById(R.id.main_btn_edit);
        searchIv = findViewById(R.id.main_iv_seeach);

        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        searchIv.setOnClickListener(this);

        setLVLongClickListener();
    }

    private void setLVLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0) {
                    return false;
                }
                int pos = position -1;
                AccountBean clickBean = mDatas.get(pos); // 获取正在被点击的这条信息

                showDeleteItemDialog(clickBean);
                return false;
            }
        });
    }

    private void showDeleteItemDialog(AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);;
        builder.setTitle("提示信息").setMessage("您确定要删除这条记录吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int click_id = clickBean.getId();
                        DBManager.deleteItemFromAccounttbById(click_id);
                        mDatas.remove(clickBean); // 实时刷新，移除集合当中的对象
                        adapter.notifyDataSetChanged(); // 提示适配器更新数据
                        setTopTvShow(); // 改变头布局textview显示的内容
                    }
                });
        builder.create().show();
    }

    private void addLVHeaderView() {
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        todayLv.addHeaderView(headerView);

        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topBudgetTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topShowIv = headerView.findViewById(R.id.item_mainlv_top_iv_hide);

        topBudgetTv.setOnClickListener(this);
        headerView.setOnClickListener(this);
        topShowIv.setOnClickListener(this);

    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
        
        setTopTvShow();
    }

    // 设置头布局当中文本内容的显示
    private void setTopTvShow() {

        float incomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 1);
        float outcomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0);
        String infoOneDay="近日指出 ￥"+outcomeOneDay+"收入 ￥"+incomeOneDay;
        topConTv.setText(infoOneDay);
        float incomeOneMonth = DBManager.getSumMoneyOneMonth(year,month,1);
        float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year,month,0);
        topInTv.setText("￥"+incomeOneMonth);
        topOutTv.setText("￥"+outcomeOneMonth);

        float bmoney = preferences.getFloat("bmoney", 0);
        if (bmoney==0) {
            topBudgetTv.setText("￥ 0");
        }else {
            float syMoney = bmoney - outcomeOneMonth;
            topBudgetTv.setText("￥"+syMoney);
        }

    }

    private void loadDBData() {
        List<AccountBean> list = DBManager.getAccountListOneDayFromAccounttb(year,month,day);
//        AccountBean actest = new AccountBean(1, "餐饮", R.mipmap.ic_canyin_fs, "备注", 125, "2021年2月9日 04:13", 2021, 2, 9, 0);
//        List<AccountBean> list = new ArrayList<>();
//        list.add(actest);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.main_iv_seeach:
                Intent it = new Intent(this, SearchActivity.class);
                startActivity(it);
                break;
            case R.id.main_btn_edit:
                Intent it1 = new Intent(this, RecordActivity.class);
                startActivity(it1);
                break;
            case R.id.main_btn_more:
                MoreDialog moreDialog = new MoreDialog(this);
                moreDialog.show();
                moreDialog.setDialogSize();
                break;
            case R.id.item_mainlv_top_tv_budget:
                showBudgetDialog();
                break;
            case R.id.item_mainlv_top_iv_hide:
                // 切换TextView明文和密文
                toggleShow();
                break;

        }
        if (v == headerView) {

            Intent intent = new Intent();
            intent.setClass(this, MonthChartActivity.class);
            startActivity(intent);

        }
    }

    // 显示运算设置对话框
    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                // 将预算金额写入共享参数当中，进行存储
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("bmoney",money);
                editor.commit();

                // 计算剩余金额
                float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
                float syMoney = money-outcomeOneMonth;
                topBudgetTv.setText("￥"+syMoney);

            }
        });
    }

    boolean isShow = true;
    private void toggleShow() {
        if (isShow){
            PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
            topInTv.setTransformationMethod(passwordMethod);
            topOutTv.setTransformationMethod(passwordMethod);
            topBudgetTv.setTransformationMethod(passwordMethod);
            topShowIv.setImageResource(R.mipmap.ih_hide);
            isShow=false;

        }else{
            HideReturnsTransformationMethod hideReturnsMethod = HideReturnsTransformationMethod.getInstance();
            topInTv.setTransformationMethod(hideReturnsMethod);
            topOutTv.setTransformationMethod(hideReturnsMethod);
            topBudgetTv.setTransformationMethod(hideReturnsMethod);
            topShowIv.setImageResource(R.mipmap.ih_show);
            isShow=true;
        }
    }
}
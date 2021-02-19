package com.example.bookkeepingdemo.frag_record;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookkeepingdemo.R;
import com.example.bookkeepingdemo.utils.BeizhuDialog;
import com.example.bookkeepingdemo.utils.KeyBoardUtils;
import com.example.bookkeepingdemo.utils.SelectTimeDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.bookkeepingdemo.db.AccountBean;
import com.example.bookkeepingdemo.db.TypeBean;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener {

    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView typeIv;
    TextView typeTv,beizhuTv,timeTv;
    GridView typeGv;
    List<TypeBean> typeBeanList;
    TypeBaseAdapter adapter;
    AccountBean accountBean; // 将需要插入到记账本的数据保存成对象形势

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBean = new AccountBean();
        accountBean.setTypename("其他");
        accountBean.setsImageId(R.mipmap.ic_qita_fs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);
        setInitTime();
        loadDataToGV();
        setGVListerner();
        return view;
    }

    // 获取当前时间，显示在timeTv上
    private void setInitTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = sdf.format(date);
        timeTv.setText(time);
        accountBean.setTime(time);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);

    }

    /***
     * 设置gridView的点击事件
     */
    private void setGVListerner() {

        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selectPos = position;
                adapter.notifyDataSetInvalidated(); //提示绘制发生变化
                TypeBean typeBean = typeBeanList.get(position);
                String typename = typeBean.getTypename();
                typeTv.setText(typename);

                accountBean.setTypename(typename);

                int sImageId = typeBean.getSimageId();
                typeIv.setImageResource(sImageId);
                accountBean.setsImageId(sImageId);
            }
        });
    }

    public void loadDataToGV() {

        typeBeanList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeBeanList);
        typeGv.setAdapter(adapter);


    }

    private void initView(View view){
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        typeIv = view.findViewById(R.id.frag_record_iv);
        typeGv = view.findViewById(R.id.frag_record_gv);
        typeTv = view.findViewById(R.id.frag_record_tv_type);
        beizhuTv = view.findViewById(R.id.frag_record_tv_beizhu);
        timeTv = view.findViewById(R.id.frag_record_tv_time);

        beizhuTv.setOnClickListener(this);
        timeTv.setOnClickListener(this);

        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView, moneyEt);
        boardUtils.showKeyboard();
        boardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {

                // 获取输入钱数
                String moneyStr = moneyEt.getText().toString();
                if (!TextUtils.isEmpty(moneyStr) || moneyStr.equals("0")) {
                    getActivity().finish();
                    return;
                }
                float money = Float.parseFloat(moneyStr);
                accountBean.setMoney(money);
                saveAccountToDB();
                getActivity().finish();
                // 点击了确定按钮
                // 获取记录的信息，保存在数据库中
                // 返回上一级页面
            }
        });



    }

    /***
     * 抽象方法： 让子类一定要重写这个方法
     */
    public abstract void saveAccountToDB() ;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.frag_record_tv_time:
                showTimeDialog();
                break;
            case R.id.frag_record_tv_beizhu:
                showBZDialog();
                break;
        }
    }

    public void showTimeDialog(){
        SelectTimeDialog dialog = new SelectTimeDialog(getContext());
        dialog.show();

        // 设定确定按钮被点击了的监听器
        dialog.setOnEnsureListener((new SelectTimeDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                timeTv.setText(time);
                accountBean.setTime(time);
                 accountBean.setYear(year);
                 accountBean.setMonth(month);
                 accountBean.setDay(day);
            }
        }));
    };

    // 弹出备注对话框
    public void showBZDialog(){

        BeizhuDialog dialog = new BeizhuDialog(getContext());
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BeizhuDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String msg = dialog.getEditText();
                if (!TextUtils.isEmpty(msg)) {
                    beizhuTv.setText(msg);
                    accountBean.setBeizhu(msg);
                }
                dialog.cancel();
            }
        });
    }
}
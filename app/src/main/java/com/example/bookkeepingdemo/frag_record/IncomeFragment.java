package com.example.bookkeepingdemo.frag_record;

import androidx.fragment.app.Fragment;

import com.example.bookkeepingdemo.R;

import java.util.List;

import com.example.bookkeepingdemo.db.DBManager;
import com.example.bookkeepingdemo.db.TypeBean;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class IncomeFragment extends BaseRecordFragment {


    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        List<TypeBean> inlist = DBManager.getTypeList(1);
        typeBeanList.addAll(inlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.in_qt_fs);

    }

    @Override
    public void saveAccountToDB() {

        accountBean.setKind(1);
        DBManager.insertToAccounttb(accountBean);
    }
}
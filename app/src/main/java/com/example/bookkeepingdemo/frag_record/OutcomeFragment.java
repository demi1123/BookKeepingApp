package com.example.bookkeepingdemo.frag_record;

import com.example.bookkeepingdemo.R;

import java.util.List;
import com.example.bookkeepingdemo.db.DBManager;
import com.example.bookkeepingdemo.db.TypeBean;

public class OutcomeFragment extends BaseRecordFragment{

    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        List<TypeBean> outlist = DBManager.getTypeList(0);
        typeBeanList.addAll(outlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.ic_qita_fs);

    }

    @Override
    public void saveAccountToDB() {

        accountBean.setKind(0);
        DBManager.insertToAccounttb(accountBean);
    }
}

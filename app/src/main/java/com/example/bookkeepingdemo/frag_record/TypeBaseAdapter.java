package com.example.bookkeepingdemo.frag_record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookkeepingdemo.R;

import java.util.List;

import com.example.bookkeepingdemo.db.TypeBean;

public class TypeBaseAdapter extends BaseAdapter {

    Context context;
    List<TypeBean> mDatas;
    int selectPos = 0;

    public TypeBaseAdapter(Context context, List<TypeBean> mDatas){
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 此适配器不考虑复用问题，因为所有的item都显示在页面上，不会因为胡奥东就消失，所以没有多余的converView，所以不用复写
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_recordfrag_gv, parent,false);
        ImageView iv = convertView.findViewById(R.id.item_recordfrag_iv);
        TextView tv = convertView.findViewById(R.id.item_recordfrag_tv);
        TypeBean typeBean = mDatas.get(position);
        tv.setText(typeBean.getTypename());
        iv.setImageResource(selectPos == position ? typeBean.getSimageId() : typeBean.getImageId());
        return convertView;
    }
}

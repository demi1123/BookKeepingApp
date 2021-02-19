package com.example.bookkeepingdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookkeepingdemo.R;
import com.example.bookkeepingdemo.db.ChartItemBean;

import java.util.List;

// 账单详情页面，Listview适配器
public class ChartItemAdapter extends BaseAdapter {

    Context context;
    List<ChartItemBean> mDatas;
    LayoutInflater inflater;

    public ChartItemAdapter(Context context, List<ChartItemBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_chart_frag_lv,parent,false);
            holder = new ViewHolder(convertView);
        }else{
            holder = (ViewHolder) convertView.getTag();

        }
        ChartItemBean chartItemBean = mDatas.get(position);
        holder.iv.setImageResource(chartItemBean.getsImageId());
        holder.typeTv.setText(chartItemBean.getType());
        holder.proTv.setText(chartItemBean.getRatio()*100 + "%");
        holder.sumTv.setText("￥"+chartItemBean.getTotalMoney());
        return convertView;
    }

    class ViewHolder{
        TextView typeTv,sumTv,proTv;
        ImageView iv;

        public ViewHolder(View view){
            typeTv = view.findViewById(R.id.item_chart_frag_tv_type);
            sumTv = view.findViewById(R.id.item_chart_frag_tv_sum);
            proTv = view.findViewById(R.id.item_chart_frag_tv_pro);
            iv = view.findViewById(R.id.item_chart_frag_iv);
        }
    }
}

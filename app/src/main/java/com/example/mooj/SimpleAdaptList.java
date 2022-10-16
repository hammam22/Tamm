package com.example.mooj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class SimpleAdaptList extends BaseAdapter {

    String list[];
    Context context;

    public SimpleAdaptList(Context context,String list[]){
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = li.inflate(R.layout.simple_list_item_1, viewGroup, false);


        TextView tv=(TextView)view.findViewById(R.id.text1);
        tv.setText(list[i]);
        /*else {
            TextView tv=(TextView)view.findViewById(R.id.text1);
            TextView tv2=(TextView)view.findViewById(R.id.tvNoBackups);
            tv.setVisibility(View.INVISIBLE);
            tv2.setVisibility(View.VISIBLE);
        }*/

        return view;
    }
}

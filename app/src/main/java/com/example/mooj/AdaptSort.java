package com.example.mooj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdaptSort extends BaseAdapter{
    ArrayList<Ashjar> arr;
    Context context;

    AdaptSort(ArrayList<Ashjar> arr,Context context){
        this.arr=arr;
        this.context=context;
    }

    public void makethetoast(String txt){
        Toast.makeText(context,txt,Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int i) {
        return arr.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class Myholder{
        TextView tvlsitem;
        LinearLayout linearLV;
        //Button btlvmaindelete;

        Myholder(View v){
            //btlvmaindelete=v.findViewById(R.id.btlvmaindelete);

            linearLV=(LinearLayout)v.findViewById(R.id.linearLV);
            tvlsitem=(TextView)v.findViewById(R.id.tvlsitem);

        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater li=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = li.inflate(R.layout.lvmainitem,viewGroup,false);

        TextView tvlsitem=(TextView)view.findViewById(R.id.tvlsitem);
        tvlsitem.setText(arr.get(i).name);

        return view;
    }
}
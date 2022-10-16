package com.example.mooj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptFav extends BaseAdapter {

    Context context;
    ArrayList<Ashjar> arr;
    boolean isFirstChecked=false,arrowDown,arrowup,isAlreadyChecked;
    int firstChecked,itemchecked;

    AdaptFav(Context context,ArrayList<Ashjar> arr){
        this.arr=arr;
        this.context=context;
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
        TextView tvprog;
        //Button btlvmaindelete;
        ProgressBar Pbar;
        LinearLayout linearLV;
        Myholder(View v){
            //btlvmaindelete=v.findViewById(R.id.btlvmaindelete);
            linearLV=(LinearLayout)v.findViewById(R.id.linearLV);
            tvlsitem=(TextView)v.findViewById(R.id.tvlsitem);
            Pbar=(ProgressBar)v.findViewById(R.id.Pbar);
            tvprog=(TextView)v.findViewById(R.id.tvprog);
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        isAlreadyChecked=false;
        View row = view;
        Myholder myholder = null;
        if (row == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = li.inflate(R.layout.lvmainitem, viewGroup, false);
            myholder = new Myholder(row);
            //myholder.btlvmaindelete=row.findViewById(R.id.btlvmaindelete);
            row.setTag(myholder);
        } else {
            myholder = (Myholder) row.getTag();
        }

        /*myholder.btlvmaindelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (context instanceof ActShajara)
                {
                    ((ActShajara)context).deletetree(arr.get(i));
                }

            }
        });*/
        myholder.tvlsitem = (TextView) row.findViewById(R.id.tvlsitem);
        if (arr.get(i).sons > 0) {
            myholder.tvlsitem.setText("(" + arr.get(i).sons + ")" + arr.get(i).name);
        } else {
            myholder.tvlsitem.setText(arr.get(i).name);
        }

        float realprog, fprog = arr.get(i).prog, fsons = arr.get(i).sons;
        if (arr.get(i).sons > 0) {
            realprog = (fprog / fsons) * 100;
        } else {
            realprog = ((int) fprog) * 100;
        }
        myholder.Pbar.setProgress((int) realprog);
        myholder.tvprog.setText(String.valueOf((int) realprog));




        return row;
    }


}

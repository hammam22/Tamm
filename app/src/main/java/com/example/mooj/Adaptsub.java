package com.example.mooj;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;


public class Adaptsub extends BaseAdapter{

    Context context;
    ArrayList<Ashjar> arr;
    ArrayList<Ashjar> exasjar=new ArrayList<Ashjar>();
    boolean isFirstChecked=false,arrowDown,arrowup,isplusCheldred,isdeletemode;
    int firstChecked,itemchecked,j=0;

    Adaptsub(Context context,ArrayList<Ashjar> arr){
        this.arr=arr;
        this.context=context;
        exasjar=((ActShajara) context).ExpandedAshjar;
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
        CheckBox chblvmain;
        ProgressBar Pbar;
        ImageView imgDown,imgUp;
        LinearLayout linearLV;
        Myholder(View v){
            //btlvmaindelete=v.findViewById(R.id.btlvmaindelete);
            linearLV=(LinearLayout)v.findViewById(R.id.linearLV);
            chblvmain=(CheckBox)v.findViewById(R.id.chblvmain);
            tvlsitem=(TextView)v.findViewById(R.id.tvlsitem);
            Pbar=(ProgressBar)v.findViewById(R.id.Pbar);
            tvprog=(TextView)v.findViewById(R.id.tvprog);
            imgDown=(ImageView) v.findViewById(R.id.imgDown);
            imgUp=(ImageView) v.findViewById(R.id.imgUp);
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

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

        if(((ActShajara) context).deletemode){
            isdeletemode=true;
        }else {
            isdeletemode=false;
        }



        if (arr.get(i).sons > 0) {
            isplusCheldred=true;

        } else {
            isplusCheldred=false;
        }

        if (arr.get(i).name.length()>22){
            myholder.tvlsitem.setTextSize(14);
            myholder.tvlsitem.setMaxLines(2);
            if (isplusCheldred){
                myholder.tvlsitem.setText("(" + arr.get(i).sons + ")" + arr.get(i).name);
            }else{
                myholder.tvlsitem.setText(arr.get(i).name);
            }
            //not more than 22 letters
        }else {
            myholder.tvlsitem.setTextSize(22);
            myholder.tvlsitem.setMaxLines(1);
            if (isplusCheldred){
                myholder.tvlsitem.setText("(" + arr.get(i).sons + ")" + arr.get(i).name);
            }else{
                myholder.tvlsitem.setText(arr.get(i).name);
            }
        }

        float realprog, fprog = arr.get(i).prog, fsons = arr.get(i).sons;
        if (arr.get(i).sons > 0) {
            realprog = (fprog / fsons) * 100;
        } else {
            realprog = ((int) fprog) * 100;
        }
        myholder.Pbar.setProgress((int) realprog);
        myholder.tvprog.setText(String.valueOf((int) realprog));



        if (arr.get(i).ab == ((ActShajara) context).id) {

            if (arr.get(i).sons > 0&&!isdeletemode) {
                myholder.imgUp.setVisibility(View.INVISIBLE);
                myholder.imgDown.setVisibility(View.VISIBLE);
                for (int j = 0; j < exasjar.size(); j++) {
                    if (arr.get(i).id == exasjar.get(j).id) {
                        myholder.imgDown.setVisibility(View.INVISIBLE);
                        myholder.imgUp.setVisibility(View.VISIBLE);
                    }
                }
            } else if(isdeletemode){
                exasjar.clear();
                arr=((ActShajara) context).abOnly;
            }else {
                myholder.imgUp.setVisibility(View.INVISIBLE);
                myholder.imgDown.setVisibility(View.INVISIBLE);
            }

            myholder.imgDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // if expanded or not
                    ((ActShajara) context).expandOrder(arr.get(i), i);
                    ((ActShajara) context).triggerExpand = true;
                    arrowup = true;
                    arrowDown = false;
                    //((MainActivity)context).oldashjar=((MainActivity)context).ashjar;
                    //Toast.makeText(((MainActivity)context),String.valueOf( ((MainActivity)context).isToDoExpand),Toast.LENGTH_SHORT).show();
                    //}

                }
            });
            myholder.imgUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ActShajara) context).dexpandAhfad(arr.get(i), i);
                    ((ActShajara) context).triggerExpand = true;
                    arrowup = false;
                    arrowDown = true;
                }
            });

            if (i == 0 && arr.get(i).sons == 0) {
                myholder.imgDown.setVisibility(View.INVISIBLE);
            }

            if (arrowDown) {
                myholder.imgUp.setVisibility(View.INVISIBLE);
                myholder.imgDown.setVisibility(View.VISIBLE);
                arrowDown = false;
            }
            if (arrowup) {
                myholder.imgDown.setVisibility(View.INVISIBLE);
                myholder.imgUp.setVisibility(View.VISIBLE);
                arrowup = false;
            }
        }


        Resources resources=context.getResources();
        //for (int k=0; k<((ActShajara)context).ExpandedAshjar.size(); k++){
            if (arr.get(i).ab!=((ActShajara)context).id){
                myholder.linearLV.setBackground(resources.getDrawable(R.drawable.sublistback));
                myholder.linearLV.setPadding(0,0,20,0);
            }
            else {
                myholder.linearLV.setBackground(resources.getDrawable(R.drawable.listback));
                myholder.linearLV.setPadding(0,0,0,0);
            }
        //}
        if (i==0){
            myholder.linearLV.setBackground(resources.getDrawable(R.drawable.listback));
            myholder.linearLV.setPadding(0,0,0,0);
        }

        if (arr.get(i).ab!=((ActShajara)context).id){
            myholder.imgDown.setVisibility(View.INVISIBLE);
            myholder.imgUp.setVisibility(View.INVISIBLE);
        }

        myholder.chblvmain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (context instanceof ActShajara)
                {
                    ((ActShajara)context).ChecklistofDelete(arr.get(i),b);
                }
            }
        });


        if(((ActShajara)context).deletemode){
            //((MainActivity)context).checklistToBeDeleted.get()
            myholder.chblvmain.setVisibility(View.VISIBLE);

            if (i==0&&firstChecked!=0&&j<22){myholder.chblvmain.setChecked(false);}
            else if (i==0&&j<22){
                myholder.chblvmain.setChecked(true);
            }
            j++;
        }




            if (i==itemchecked){
                myholder.chblvmain.setChecked(!myholder.chblvmain.isChecked());
            }
            //if (i==0&&itemchecked!=0){
            //   myholder.chblvmain.setChecked(!myholder.chblvmain.isChecked());}



        return row;
    }

    void setFirstChecked(int index){
        isFirstChecked=true;
        firstChecked =index;
    }


    void setchecked(int index){
        itemchecked=index;
    }

    void setArrowDown(){
        arrowDown=true;
    }

    void setArrowUp(){
        arrowup=true;
    }

}

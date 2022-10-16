package com.example.mooj;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;

public class FragFromDtata extends Fragment {


    ArrayList<String> list=new ArrayList<String>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_from_data,container,false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        File sd= Environment.getExternalStorageDirectory();
        File moojDir=new File(sd.getAbsolutePath()+"/Android/data/mooj");
        if (moojDir.exists()){

            File files[]=moojDir.listFiles();

            for (int i=0; i<files.length; i++){
                list.add(files[i].getName());
            }

        }else {
            Toast.makeText(getActivity(),"لا توجد نسخ سابقة",Toast.LENGTH_SHORT).show();
        }

        ListView lvFraFromData=(ListView)view.findViewById(R.id.lvFraFromData);
        if (list.size()>0){
        String mylist[] =new String[list.size()];
        mylist = list.toArray(mylist);
        SimpleAdaptList simpleAdaptList=new SimpleAdaptList(getActivity(),mylist);
        lvFraFromData.setAdapter(simpleAdaptList);
            String[] finalMylist = mylist;
            lvFraFromData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intrFromData intrData=(intrFromData)getContext();
                intrData.getDbFromPath(finalMylist[i],false);
            }
        });
            }else {
            lvFraFromData.setVisibility(View.INVISIBLE);
            TextView tvNoBackups=(TextView)view.findViewById(R.id.tvNoBackups);
            tvNoBackups.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public interface intrFromData{
        void  getDbFromPath(String path,boolean ispath);
    };

}

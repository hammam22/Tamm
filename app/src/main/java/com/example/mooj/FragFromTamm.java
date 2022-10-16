package com.example.mooj;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;

public class FragFromTamm extends Fragment {

    File sd,files[];
    ListView lvFramFromTamm;
    Button btback_fromImpo;
    SimpleAdaptList adaptList;
    Context context;
    ArrayList<String> list=new ArrayList<String>();
    ArrayList<File> listfiles=new ArrayList<File>();
    ArrayList<File> pathIndex=new ArrayList<File>();
    intrFromTamm intrFromTamm;

    public FragFromTamm(Context context){
        this.context=context;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fra_from_tamm,container,false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sd = Environment.getExternalStorageDirectory();

        lvFramFromTamm=(ListView)view.findViewById(R.id.lvFramFromTamm);
        lvFramFromTamm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (listfiles.get(i).isDirectory()){
                    pathIndex.add(sd);
                    sd=listfiles.get(i);
                    getFiles();
                    checkvisability();
                }else {
                    String fullname=listfiles.get(i).getName();
                    String exten=fullname.substring(fullname.length()-4);
                    if (exten.equals(".tmm")){
                        intrFromTamm=(FragFromTamm.intrFromTamm)getContext();
                        intrFromTamm.getDbFromPath(listfiles.get(i).getAbsolutePath(),true);
                    }
                }

            }
        });

        btback_fromImpo=(Button)view.findViewById(R.id.btback_fromImpo);
        btback_fromImpo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sd=pathIndex.get(pathIndex.size()-1);
                pathIndex.remove(pathIndex.size()-1);
                getFiles();
                checkvisability();
            }
        });

        getFiles();

    }

    private void checkvisability() {
        if (pathIndex.size()>0){
            btback_fromImpo.setVisibility(View.VISIBLE);
        }else {
            btback_fromImpo.setVisibility(View.INVISIBLE);
        }
    }

    private void getFiles() {
        File files[]=sd.listFiles();

        listfiles.clear();
        for (int j=0; j<files.length; j++){
            listfiles.add(files[j]);
        }

        list.clear();
        for (int i=0; i<files.length; i++){
            list.add(files[i].getName());
        }
        String mylist[]=new String[list.size()];
        mylist=list.toArray(mylist);

        adaptList=new SimpleAdaptList(context,mylist);
        lvFramFromTamm.setAdapter(adaptList);
    }

    public interface intrFromTamm{
        void  getDbFromPath(String path,boolean ispath);
    };
}

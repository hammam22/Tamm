package com.example.mooj;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class DiaExport extends AppCompatDialogFragment {

    Context context;
    File sd;
    File files[];
    ArrayList<String> list=new ArrayList<String>();
    ArrayList<File> onlyDirs=new ArrayList<File>();
    ArrayList<File> mypath=new ArrayList<File>();
    SimpleAdaptList adaptList;
    ListView lvexport;
    Button btCAncelExport,btKeepExport,btback_fromExpo;
    EditText ed_export;
    boolean isEveryThing;


    public DiaExport(Context context,boolean isEveryThing){
        this.context=context;
        this.isEveryThing=isEveryThing;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dia_export, null);

        lvexport=(ListView)view.findViewById(R.id.lvexport);

        sd = Environment.getExternalStorageDirectory();
        files=sd.listFiles();

        for (int k=0; k<files.length; k++){
            if (files[k].isDirectory()){
                onlyDirs.add(files[k]);
             }
        }

        makelist();

        lvexport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mypath.add(sd);
                getPathIndex();

                String newstep = onlyDirs.get(i).getAbsolutePath();
                sd=new File(newstep);
                files=sd.listFiles();
                onlyDirs.clear();
                for (int j=0; j<files.length;j++){
                    if (files[j].isDirectory()){
                        onlyDirs.add(files[j]);
                    }
                }
                makelist();

            }
        });

        ed_export=(EditText)view.findViewById(R.id.ed_export);

        btKeepExport=(Button)view.findViewById(R.id.btKeepExport);
        btKeepExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    File data=Environment.getDataDirectory();


                    String fileName;
                    try {
                        fileName= ed_export.getText().toString();
                    }catch (Exception e){
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                        fileName="";
                    }

                    if (sd.canWrite()&&!fileName.isEmpty()){
                        String pathdst="";
                        String pathscr="";
                        if (isEveryThing){ pathscr="/data/"+data.getAbsolutePath()+"/com.example.mooj/databases/mydb";
                        } else {
                            pathscr="/data/"+data.getAbsolutePath()+"/com.example.mooj/databases/share";
                        }



                        pathdst=sd.getAbsolutePath()+"/"+fileName+".tmm";


                        //Toast.makeText(this,getDate(),Toast.LENGTH_SHORT).show();

                        File scr=new File(pathscr);
                        File dst=new File(pathdst);
                        if (scr.exists()){
                            try {

                                FileChannel myscr=new FileInputStream(scr).getChannel();
                                FileChannel mydst=new FileOutputStream(dst).getChannel();
                                mydst.transferFrom(myscr,0,myscr.size());
                                mydst.close();
                                myscr.close();

                                Toast.makeText(context,"تم التخزين",Toast.LENGTH_SHORT).show();
                                //delete share
                                pathscr="/data/"+data.getAbsolutePath()+"/com.example.mooj/databases/share";
                                File share = new File(pathscr);
                                if (share.exists()){
                                    share.delete();
                                }
                                dismiss();

                            } catch (FileNotFoundException e) {
                                Toast.makeText(context,"خطأ في المزامنة",Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else {Toast.makeText(context,"لا توجد بيانات لمزامنتها",Toast.LENGTH_SHORT).show(); }
                    }
                    else if (fileName.isEmpty()){
                        Toast.makeText(context,"اكتب اسم الملف",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context,"لا يمكن الوصول للذاكرة",Toast.LENGTH_SHORT).show();
                    }
                }

        });

        btCAncelExport=(Button)view.findViewById(R.id.btCAncelExport);
        btCAncelExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btback_fromExpo=(Button)view.findViewById(R.id.btback_fromExpo);
        btback_fromExpo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sd = mypath.get(mypath.size()-1);
                mypath.remove(mypath.size()-1);

                files=sd.listFiles();
                onlyDirs.clear();
                for (int j=0; j<files.length;j++){
                    if (files[j].isDirectory()){
                        onlyDirs.add(files[j]);
                    }
                }
                makelist();
                getPathIndex();
            }
        });

        builder.setView(view);

        return builder.create();

    }

    private void getPathIndex() {
        if (mypath.size()>0){
            btback_fromExpo.setVisibility(View.VISIBLE);
        }else{
            btback_fromExpo.setVisibility(View.INVISIBLE);
        }
    }


    void makelist() {
        list.clear();
        /*for (int i=0; i<onlyDirs.size(); i++){
            list.add(onlyDirs[i].getName());
        }
        */
        String mylist[]=new String[onlyDirs.size()];
        ArrayList<String> dirsString= new ArrayList<String>();
        for (int l=0; l<onlyDirs.size(); l++){
            dirsString.add(onlyDirs.get(l).getName());
        }
        mylist = dirsString.toArray(mylist);
        adaptList= new SimpleAdaptList(context,mylist);
        lvexport.setAdapter(adaptList);
    }

}

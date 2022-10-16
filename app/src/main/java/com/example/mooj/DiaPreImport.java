package com.example.mooj;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

import java.io.File;

public class DiaPreImport extends AppCompatDialogFragment {

    int total,matches;
    Context context;
    File file;

    public DiaPreImport(int total, int matches,File file, Context context){
        this.total=total;
        this.context=context;
        this.matches=matches;
        this.file=file;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dia_pre_import,null);

        TextView tvPreImMatches = (TextView)view.findViewById(R.id.tvPreImMatches);
        tvPreImMatches.setText(matches+" تكرار على الاقل");

        TextView tvPreImTotal = (TextView)view.findViewById(R.id.tvPreImTotal);
        tvPreImTotal.setText(total+" عنصر");
        
        Button btcan=(Button)view.findViewById(R.id.btCAncelPreIm);
        btcan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        
        Button btConti = (Button)view.findViewById(R.id.btContinuelPreIm);
        btConti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ActImport)context).addtoDB(file);
                dismiss();
            }
        });

        builder.setView(view);

        return builder.create();
    }
}

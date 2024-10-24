package com.android.ricendetectwithxml;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    Button btn_about, btn_help, btn_quickIdentify, btn_fields;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_fields = findViewById(R.id.btnfields);
        btn_quickIdentify = findViewById(R.id.btnquickidentify);
        btn_about = findViewById(R.id.btnabout);
        btn_help = findViewById(R.id.btnhelp);

        btn_about.setOnClickListener(v -> showAboutDialog());
        btn_help.setOnClickListener(v -> showHelpDialog());

        btn_fields.setOnClickListener(v -> {
            intent = new Intent(MainActivity.this, FieldActivity.class);
            startActivity(intent);
        });

        btn_quickIdentify.setOnClickListener(v -> {
            intent = new Intent(MainActivity.this, NitrogenIdentifierLayout.class);
            startActivity(intent);
        });
    }

    private void showHelpDialog() {
        // Inflate the help layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View inflatedLayout = inflater.inflate(R.layout.fragment_help, null);

        // Build the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(inflatedLayout);

        // Optionally, you can add buttons if needed
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Create and show the dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button btn_howAddField = inflatedLayout.findViewById(R.id.btn_howAddField);
        btn_howAddField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View inflatedLayout = inflater.inflate(R.layout.add_field_help_layout, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(inflatedLayout);

                builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }


    private void showAboutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("About RiceNDetect")
                .setMessage("Description: This app is for the development of the research application made by the students of the PRMSU in partially fulfillment of Bachelor Science in Computer Science\n\n\n" +
                        "Email:hunsendrake@gmail.com\n" +
                        "Contact info:09065904571\n\n\n" +
                        "v 1.0")
                .setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
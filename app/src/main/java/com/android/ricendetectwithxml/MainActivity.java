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
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

        // Create and show the dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button btn_howAddField = inflatedLayout.findViewById(R.id.btn_howAddField);
        btn_howAddField.setOnClickListener(v -> {

            alertDialog.dismiss();
            LayoutInflater inflater1 = LayoutInflater.from(MainActivity.this);
            View inflatedLayout1 = inflater1.inflate(R.layout.add_field_help_layout, null);

            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
            builder1.setView(inflatedLayout1);

            builder1.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

            AlertDialog alertDialog1 = builder1.create();
            alertDialog1.show();
        });

        Button btn_howEditFIeld = inflatedLayout.findViewById(R.id.btneditfield);
        btn_howEditFIeld.setOnClickListener(v -> {
            alertDialog.dismiss();
            LayoutInflater inflater1 = LayoutInflater.from(MainActivity.this);
            View inflatedLayout1 = inflater1.inflate(R.layout.edit_field_help_layout, null);

            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
            builder1.setView(inflatedLayout1);

            builder1.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

            AlertDialog alertDialog1 = builder1.create();
            alertDialog1.show();
        });

        Button btn_howDeleteField = inflatedLayout.findViewById(R.id.btndeletefield);
        btn_howDeleteField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                LayoutInflater inflater1 = LayoutInflater.from(MainActivity.this);
                View inflatedLayout1 = inflater1.inflate(R.layout.delete_field_help_layout, null);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setView(inflatedLayout1);

                builder1.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
            }
        });
    }


    private void showAboutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("About")
                .setMessage("Description: This app is for the development of the research application made by the students of the President Ramon Magsaysay State University in partially fulfillment of Bachelor Science in Computer Science\n\n\n" +
                        "Email:hunsendrake@gmail.com\n" +
                        "Contact info:09065904571\n\n\n" +
                        "v 1.0")
                .setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
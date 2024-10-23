package com.android.ricendetectwithxml;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
        btn_help.setOnClickListener(v -> inflateHelpLayout());

        btn_fields.setOnClickListener(v -> {
            intent = new Intent(MainActivity.this, FieldActivity.class);
            startActivity(intent);
        });

        btn_quickIdentify.setOnClickListener(v -> {
            intent = new Intent(MainActivity.this, NitrogenIdentifierLayout.class);
            startActivity(intent);
        });
    }

    private void inflateHelpLayout(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflatedLayout = inflater.inflate(R.layout.fragment_help, null);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        RelativeLayout rootLayout = findViewById(R.id.homelayout);
        rootLayout.addView(inflatedLayout, params);
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
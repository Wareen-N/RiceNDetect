package com.android.ricendetectwithxml;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class NitrogenIdentifierLayout extends AppCompatActivity {

    private static final int[] imageViewIds = {
            R.id.firstPic, R.id.secondPic, R.id.thirdPic,
            R.id.fourthPic, R.id.fifthPic, R.id.sixthPic,
            R.id.seventhPic, R.id.eightPic, R.id.ninthPic,
            R.id.tenthPic, R.id.eleventhPic, R.id.twelftPic
    };

    private ArrayList<Bitmap> imagesToClassify;
    private Spinner spin_fieldUnitArea, spin_areaValue;
    private TextView txt_DATOrDASDate;
    private TextView txt_fertilizerApplicationDate;
    private Button btn_confirm;
    private ImageView imv_historyOpener;

    public String fieldId, fieldName, fieldUnitArea, fieldAreaValue, fieldDasDat, fieldLastDateFertilizer;
    public TextView txt_layoutTitle;

    boolean isInDATorDAS = false;
    private ImageView imgv_currentImageView;
    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nitrogen_identifier_layout);
        spin_fieldUnitArea = findViewById(R.id.unitArea);
        spin_areaValue = findViewById(R.id.valueCoresspondToUnitArea);
        txt_DATOrDASDate = findViewById(R.id.dateForDATOrDAS);
        txt_fertilizerApplicationDate = findViewById(R.id.lastFertilizerApplicationDate);
        btn_confirm = findViewById(R.id.btnConfirmIdentify);
        txt_layoutTitle = findViewById(R.id.fieldTitle);
        imv_historyOpener = findViewById(R.id.imv_historyOpener);

        imv_historyOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ll_dateDetails = findViewById(R.id.ll_dateDetails);
                ll_dateDetails.setVisibility(View.VISIBLE);


                imv_historyOpener.setBackground(ContextCompat.getDrawable(NitrogenIdentifierLayout.this, R.drawable.baseline_keyboard_arrow_up_24));
            }
        });

        txt_DATOrDASDate.setOnClickListener(v -> {
            isInDATorDAS = true;
            showDatePickerDialog();
        });

        txt_fertilizerApplicationDate.setOnClickListener(v -> {
            isInDATorDAS = false;
            showDatePickerDialog();
        });

        btn_confirm.setOnClickListener(v ->{
            Intent intent = new Intent(NitrogenIdentifierLayout.this, Result.class);
            startActivity(intent);
        });

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        populateTheUnitAreaAndValueCorrespondsToUnitAreaSpinners();
        fetchDataInSelectedFieldCardViewInFieldLayout();

        for (int imageViewId : imageViewIds) {
            ImageView imageView = findViewById(imageViewId);
            imageView.setOnClickListener(v -> {
                imgv_currentImageView = imageView;  //getting the id of the imageview that is clicked then storing to current image view for the usage in on activity Result function to address the image where it iwlll upload
                openGallery();
            });
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    private void classifyImage(Bitmap bitmap){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                int dimension = Math.min(bitmap.getWidth(), bitmap.getHeight());
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension);
                imgv_currentImageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void fetchDataInSelectedFieldCardViewInFieldLayout(){
        if (getIntent().hasExtra("fieldId") &&
                getIntent().hasExtra("fieldName") &&
                getIntent().hasExtra("fieldId") &&
                getIntent().hasExtra("fieldUnitArea") &&
                getIntent().hasExtra("fieldAreaValue") &&
                getIntent().hasExtra("fieldDasDat") &&
                getIntent().hasExtra("fieldLastDateFertilizer")){


            fieldId = getIntent().getStringExtra("fieldId");
            fieldName = getIntent().getStringExtra("fieldName");
            fieldUnitArea = getIntent().getStringExtra("fieldUnitArea");
            fieldAreaValue = getIntent().getStringExtra("fieldAreaValue");
            fieldDasDat = getIntent().getStringExtra("fieldDasDat");
            fieldLastDateFertilizer = getIntent().getStringExtra("fieldLastDateFertilizer");

            //disabling the important objects
            spin_areaValue.setEnabled(false);
            spin_fieldUnitArea.setEnabled(false);
            txt_DATOrDASDate.setEnabled(false);
            txt_fertilizerApplicationDate.setEnabled(false);

            //setting up and putting the value in the spinners
            populateTheUnitAreaAndValueCorrespondsToUnitAreaSpinners();
            setAreaToSpinners();

            txt_DATOrDASDate.setText(fieldDasDat);
            txt_fertilizerApplicationDate.setText(fieldLastDateFertilizer);
            txt_layoutTitle.setText(fieldName);

        }
    }

    private void setAreaToSpinners(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.unitArea, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_fieldUnitArea.setAdapter(adapter);
        int position = adapter.getPosition(fieldUnitArea);
        spin_fieldUnitArea.setSelection(position);
    }


    private void populateTheUnitAreaAndValueCorrespondsToUnitAreaSpinners(){
        //for unit area spinner
        ArrayAdapter<CharSequence> adapterForUnitArea = ArrayAdapter.createFromResource(this,
                R.array.unitArea, android.R.layout.simple_spinner_item);
        adapterForUnitArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_fieldUnitArea.setAdapter(adapterForUnitArea);

        //for value correspond to unit area spinner
        ArrayList<String> numberList = new ArrayList<>();
        for (int i = 1; i <= 200; i++) {
            numberList.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapterForValue = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, numberList);
        adapterForValue.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_areaValue.setAdapter(adapterForValue);

        if (fieldAreaValue != null && !fieldAreaValue.isEmpty()) {
            // Find the position of fieldAreaValue in the numberList
            int position = numberList.indexOf(fieldAreaValue);
            if (position >= 0) {
                // Set the selection of the spinner to the matching value
                spin_areaValue.setSelection(position);
            }
        }
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();

        //Initialize DatePicker with the current date
        DatePicker datePicker = new DatePicker(this);
        datePicker.init(
                c.get(Calendar.YEAR),//get current year
                c.get(Calendar.MONTH),//get current month
                c.get(Calendar.DAY_OF_MONTH),//get current date
                null
        );

        //Create AlertDialog with the DatePicker as its view
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(datePicker);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the selected date from DatePicker
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();

                // Set the selected date in the TextView

                String dateTemp = "";
                if((month + 1) <= 9){
                    dateTemp = "0" + (month + 1) + "/" + day  + "/" + year;
                }else{
                    dateTemp = (month + 1) + "/" + day  + "/" + year;
                }

                if(!isInDATorDAS){
                    txt_fertilizerApplicationDate.setText(dateTemp);
                }else{
                    txt_DATOrDASDate.setText(dateTemp);
                }

            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
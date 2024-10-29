package com.android.ricendetectwithxml;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


public class FieldActivity extends AppCompatActivity {

    private TextView DATOrDASDate, fertilizerApplicationDate;
    private Boolean isInDATorDAS = false;
    private Spinner unitArea, valueCorrespondToUnitArea;
    private FieldDB fieldDB;
    private FieldCustomAdapter fieldCustomAdapter;
    private RecyclerView rvw_fieldViewer;
    private ArrayList<String> fieldId, fieldName, fieldUnitArea, fieldAreaValue, fieldDasDat, fieldLastDateFertilizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field);
        fieldDB = new FieldDB(this);
        fieldId = new ArrayList<>();
        fieldName = new ArrayList<>();
        fieldUnitArea = new ArrayList<>();
        fieldAreaValue = new ArrayList<>();
        fieldDasDat = new ArrayList<>();
        fieldLastDateFertilizer = new ArrayList<>();

        rvw_fieldViewer = findViewById(R.id.fieldsViewContainer);

        //displaying the fields data using custom adapter
        storeFieldDataInArrayList();
        fieldCustomAdapter = new FieldCustomAdapter(FieldActivity.this, fieldId, fieldName, fieldUnitArea, fieldAreaValue, fieldDasDat, fieldLastDateFertilizer);
        rvw_fieldViewer.setAdapter(fieldCustomAdapter);
        rvw_fieldViewer.setLayoutManager(new LinearLayoutManager(FieldActivity.this));

        Button btnAddField = findViewById(R.id.btnAddField);
        btnAddField.setOnClickListener(v -> inflateFieldAdderLayout());
    }

    private void inflateFieldAdderLayout(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflatedLayout = inflater.inflate(R.layout.fieldadderlayout, null);

        //accessing the component inside
        EditText et_fieldName = inflatedLayout.findViewById(R.id.disp_fieldName);
        DATOrDASDate = inflatedLayout.findViewById(R.id.dateForDATOrDAS);
        fertilizerApplicationDate = inflatedLayout.findViewById(R.id.lastFertilizerApplicationDate);
        unitArea = inflatedLayout.findViewById(R.id.unitArea);
        valueCorrespondToUnitArea = inflatedLayout.findViewById(R.id.valueCoresspondToUnitArea);
        Button btnConfirmAddField = inflatedLayout.findViewById(R.id.btnConfirmAddField);
        TextView btnCancelAddField = inflatedLayout.findViewById(R.id.btnCancelAddField);
        FrameLayout fieldAdder = inflatedLayout.findViewById(R.id.fieldAdder);

        populateTheUnitAreaAndValueCorrespondsToUnitAreaSpinners();
        DATOrDASDate.setOnClickListener(v -> {
            isInDATorDAS = true;
            showDatePickerDialog();
        });

        fertilizerApplicationDate.setOnClickListener(v -> {
            isInDATorDAS = false;
            showDatePickerDialog();
        });

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        RelativeLayout rootLayout = findViewById(R.id.fieldHome);
        rootLayout.addView(inflatedLayout, params);

        btnCancelAddField.setOnClickListener(v -> rootLayout.removeView(fieldAdder));

        btnConfirmAddField.setOnClickListener(v -> {
            String fieldNameText = et_fieldName.getText().toString();
            String selectedDATOrDAS = DATOrDASDate.getText().toString();
            String selectedFertilizerDate = fertilizerApplicationDate.getText().toString();
            int selectedUnitAreaId = (int) unitArea.getSelectedItemId();
            int selectedAreaValueId = (int) valueCorrespondToUnitArea.getSelectedItemId();

            if (fieldNameText.length() <= 4 || fieldNameText.length() >= 15) {
                Toast.makeText(getBaseContext(), "Field name must be > 4 and < 15 characters", Toast.LENGTH_SHORT).show();
            }
            else if (selectedDATOrDAS.equals("Selected Date")) {
                Toast.makeText(getBaseContext(), "Select DAT/DAS date", Toast.LENGTH_SHORT).show();
            }
            else if (selectedFertilizerDate.equals("Selected Date")) {
                Toast.makeText(getBaseContext(), "Select fertilizer application date", Toast.LENGTH_SHORT).show();
            }
            else {
                fieldDB = new FieldDB(getBaseContext());

                long newFieldId = fieldDB.addFieldData(fieldNameText,
                        unitArea.getItemAtPosition(selectedUnitAreaId).toString(),
                        valueCorrespondToUnitArea.getItemAtPosition(selectedAreaValueId).toString(),
                        selectedDATOrDAS,
                        selectedFertilizerDate);

                fieldId.add(String.valueOf(newFieldId));
                fieldName.add(fieldNameText);
                fieldUnitArea.add(unitArea.getItemAtPosition(selectedUnitAreaId).toString());
                fieldAreaValue.add(valueCorrespondToUnitArea.getItemAtPosition(selectedAreaValueId).toString());
                fieldDasDat.add(selectedDATOrDAS);
                fieldLastDateFertilizer.add(selectedFertilizerDate);

                fieldCustomAdapter.notifyItemInserted(fieldId.size() - 1);

                rootLayout.removeView(fieldAdder);
            }
        });
    }

    private void populateTheUnitAreaAndValueCorrespondsToUnitAreaSpinners(){
        //for unit area spinner
        ArrayAdapter<CharSequence> adapterForUnitArea = ArrayAdapter.createFromResource(this,
                R.array.unitArea, android.R.layout.simple_spinner_item);
        adapterForUnitArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitArea.setAdapter(adapterForUnitArea);

        //for value correspond to unit area spinner
        ArrayList<String> numberList = new ArrayList<>();
        for (int i = 1; i <= 200; i++) {
            numberList.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapterForValue = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, numberList);
        adapterForValue.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        valueCorrespondToUnitArea.setAdapter(adapterForValue);
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();

        // Initialize DatePicker with the current date
        DatePicker datePicker = new DatePicker(this);
        datePicker.init(
                c.get(Calendar.YEAR),//get current year
                c.get(Calendar.MONTH),//get current month
                c.get(Calendar.DAY_OF_MONTH),//get current date
                null
        );

        // Create AlertDialog with the DatePicker as its view
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(datePicker);
        builder.setPositiveButton("OK", (dialog, which) -> {
            // Get the selected date from DatePicker
            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int day = datePicker.getDayOfMonth();

            // Set the selected date in the TextView

            String dateTemp;
            if((month + 1) <= 9){
                dateTemp = "0" + (month + 1) + "/" + day  + "/" + year;
            }else{
                dateTemp = (month + 1) + "/" + day  + "/" + year;
            }

            if(!isInDATorDAS){
                fertilizerApplicationDate.setText(dateTemp);
            }else{
                DATOrDASDate.setText(dateTemp);
            }

        });
        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    public void storeFieldDataInArrayList(){
        ImageView imv_noData = findViewById(R.id.imv_noData);
        Cursor cursor = fieldDB.readAllFieldData();
        if(cursor.getCount() == 0){
            imv_noData.setVisibility(View.VISIBLE);
        }else{
            imv_noData.setVisibility(View.INVISIBLE);
            while(cursor.moveToNext()){
                fieldId.add(cursor.getString(0));
                fieldName.add(cursor.getString(1));
                fieldUnitArea.add(cursor.getString(2));
                fieldAreaValue.add(cursor.getString(3));
                fieldDasDat.add(cursor.getString(4));
                fieldLastDateFertilizer.add(cursor.getString(5));
            }
        }
    }
}
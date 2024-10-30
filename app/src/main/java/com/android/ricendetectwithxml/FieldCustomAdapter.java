package com.android.ricendetectwithxml;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class FieldCustomAdapter extends RecyclerView.Adapter<FieldCustomAdapter.FieldViewHolder>{
    private Context context;
    private ArrayList<String> fieldId, fieldName, fieldUnitArea, fieldAreaValue, fieldDasDat, fieldLastDateFertilizer;
    private Button btn_editField, btn_deleteField, btn_editConfirm;
    private FieldDB fieldDB;
    private TextView btn_editCancel;
    private Boolean isInDATorDAS;

    public interface DeletionConfirmationListener {
        void onConfirm();
        void onCancel();
    }

    public FieldCustomAdapter(Context context,
                              ArrayList<String> fieldId,
                              ArrayList<String> fieldName,
                              ArrayList<String> fieldUnitArea,
                              ArrayList<String> fieldAreaValue,
                              ArrayList<String> fieldDasDat,
                              ArrayList<String> fieldLastDateFertilizer) {
        this.context = context;
        this.fieldId = fieldId;
        this.fieldName = fieldName;
        this.fieldUnitArea = fieldUnitArea;
        this.fieldAreaValue = fieldAreaValue;
        this.fieldDasDat = fieldDasDat;
        this.fieldLastDateFertilizer = fieldLastDateFertilizer;
    }

    @NonNull
    @Override
    public FieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_field_card_design, parent, false);
        return new FieldViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FieldViewHolder holder, int position) {
        holder.txt_fieldId.setText(String.valueOf(fieldId.get(position)));
        holder.txt_fieldName.setText(String.valueOf(fieldName.get(position)));
        holder.txt_fieldUnitArea.setText(String.valueOf(fieldUnitArea.get(position)));
        holder.txt_fieldAreaValue.setText(String.valueOf(fieldAreaValue.get(position)));
        holder.txt_fieldDasDat.setText(String.valueOf(fieldDasDat.get(position)));
        holder.txt_fieldLastDateFertilizer.setText(String.valueOf(fieldLastDateFertilizer.get(position)));


        holder.cvw_fieldCardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NitrogenIdentifierLayout.class);
            intent.putExtra("fieldId", String.valueOf(fieldId.get(position)));
            intent.putExtra("fieldName", String.valueOf(fieldName.get(position)));
            intent.putExtra("fieldUnitArea", String.valueOf(fieldUnitArea.get(position)));
            intent.putExtra("fieldAreaValue", String.valueOf(fieldAreaValue.get(position)));
            intent.putExtra("fieldDasDat", String.valueOf(fieldDasDat.get(position)));
            intent.putExtra("fieldLastDateFertilizer", String.valueOf(fieldLastDateFertilizer.get(position)));
            context.startActivity(intent);
        });

        fieldDB = new FieldDB(context);
        btn_deleteField.setOnClickListener(v -> {

            showDeletionConfirmation(String.valueOf(fieldName.get(position)), new DeletionConfirmationListener() {
                @Override
                public void onConfirm() {
                    fieldDB.deleteOneRow(String.valueOf(fieldId.get(position)));
                    fieldId.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, fieldId.size());
                }

                @Override
                public void onCancel() {

                }
            });
        });

        btn_editField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditFieldDialog(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fieldId.size();
    }

    public class FieldViewHolder extends RecyclerView.ViewHolder {
        TextView txt_fieldId, txt_fieldName, txt_fieldUnitArea, txt_fieldAreaValue, txt_fieldDasDat, txt_fieldLastDateFertilizer;
        CardView cvw_fieldCardView;
        public FieldViewHolder(@NonNull View itemView) {
            super(itemView);
            cvw_fieldCardView = itemView.findViewById(R.id.fieldCardView);
            txt_fieldId = itemView.findViewById(R.id.disp_fieldId);
            txt_fieldName = itemView.findViewById(R.id.disp_fieldName);
            txt_fieldUnitArea = itemView.findViewById(R.id.disp_fieldUnitArea);
            txt_fieldAreaValue = itemView.findViewById(R.id.disp_fieldAreaValue);
            txt_fieldDasDat = itemView.findViewById(R.id.disp_dateForDATDAS);
            txt_fieldLastDateFertilizer = itemView.findViewById(R.id.disp_dateForLastFertilizer);

            btn_editField = itemView.findViewById(R.id.btnEditField);
            btn_deleteField = itemView.findViewById(R.id.btnDeleteField);
        }
    }

    private void showEditFieldDialog(int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.fieldadderlayout, null);

        EditText edt_fieldName = dialogView.findViewById(R.id.disp_fieldName);
        TextView txt_layoutTitle = dialogView.findViewById(R.id.layoutTitle);
        Spinner spin_fieldUnitArea = dialogView.findViewById(R.id.unitArea);
        Spinner spin_fieldAreaValue = dialogView.findViewById(R.id.valueCoresspondToUnitArea);
        TextView txt_fieldDatDas = dialogView.findViewById(R.id.dateForDATOrDAS);
        TextView txt_fieldLastDateFertilizer = dialogView.findViewById(R.id.lastFertilizerApplicationDate);
        btn_editCancel = dialogView.findViewById(R.id.btnCancelAddField);
        btn_editConfirm = dialogView.findViewById(R.id.btnConfirmAddField);

        TextView lbl_datdas = dialogView.findViewById(R.id.lbl_datdas);
        TextView lbl_dateFertilizerApplication = dialogView.findViewById(R.id.lbl_lastDateFertilizerApplication);

        txt_layoutTitle.setText("Edit Field");
        txt_layoutTitle.setTextSize(20);

        edt_fieldName.setTextSize(15);

        lbl_datdas.setTextSize(15);
        lbl_dateFertilizerApplication.setTextSize(15);

        txt_fieldDatDas.setTextSize(15);
        txt_fieldLastDateFertilizer.setTextSize(15);

        // Pre-fill the fields with existing data
        edt_fieldName.setText(String.valueOf(fieldName.get(position)));
        txt_fieldDatDas.setText(String.valueOf(fieldDasDat.get(position)));
        txt_fieldLastDateFertilizer.setText(String.valueOf(fieldLastDateFertilizer.get(position)));
        populateTheUnitAreaAndValueCorrespondsToUnitAreaSpinners(spin_fieldUnitArea,spin_fieldAreaValue, position);
        setAreaToSpinners(spin_fieldUnitArea, String.valueOf(fieldUnitArea.get(position)));

        // Build the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);  // Set the custom view in the dialog
        AlertDialog alertDialog = builder.create();

        btn_editConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Updating")
                        .setMessage("Are you sure you want to Update: " + fieldName.get(position) + "?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fieldDB.updateFieldData(String.valueOf(fieldId.get(position)),
                                        edt_fieldName.getText().toString(),
                                        spin_fieldUnitArea.getSelectedItem().toString(),
                                        spin_fieldAreaValue.getSelectedItem().toString(),
                                        txt_fieldDatDas.getText().toString(),
                                        txt_fieldLastDateFertilizer.getText().toString());

                                // Update the ArrayList with the new data
                                fieldName.set(position, edt_fieldName.getText().toString());
                                fieldUnitArea.set(position, spin_fieldUnitArea.getSelectedItem().toString());
                                fieldAreaValue.set(position, spin_fieldAreaValue.getSelectedItem().toString());
                                fieldDasDat.set(position, txt_fieldDatDas.getText().toString());
                                fieldLastDateFertilizer.set(position, txt_fieldLastDateFertilizer.getText().toString());

                                // Notify the adapter of the item change at the correct position
                                notifyItemChanged(position);

                                alertDialog.dismiss();
                                Toast.makeText(context, "Update successful!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btn_editCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        txt_fieldDatDas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInDATorDAS = true;
                showDatePickerDialog(txt_fieldDatDas, txt_fieldLastDateFertilizer);
            }
        });

        txt_fieldLastDateFertilizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isInDATorDAS = false;
                showDatePickerDialog(txt_fieldDatDas, txt_fieldLastDateFertilizer);
            }
        });

        alertDialog.show();
    }

    private void populateTheUnitAreaAndValueCorrespondsToUnitAreaSpinners(Spinner spin_fieldUnitArea, Spinner spin_areaValue, int rPosition){
        //for unit area spinner
        ArrayAdapter<CharSequence> adapterForUnitArea = ArrayAdapter.createFromResource(context,
                R.array.unitArea, android.R.layout.simple_spinner_item);
        adapterForUnitArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_fieldUnitArea.setAdapter(adapterForUnitArea);

        //for value correspond to unit area spinner
        ArrayList<String> numberList = new ArrayList<>();
        for (int i = 1; i <= 200; i++) {
            numberList.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapterForValue = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, numberList);
        adapterForValue.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_areaValue.setAdapter(adapterForValue);

        if (fieldAreaValue != null && !fieldAreaValue.isEmpty()) {
            // Find the position of fieldAreaValue in the numberList
            int position = numberList.indexOf(String.valueOf(fieldAreaValue.get(rPosition)));
            if (position >= 0) {
                // Set the selection of the spinner to the matching value
                spin_areaValue.setSelection(position);
            }
        }
    }

    public void addField(String id, String name, String unitArea, String areaValue, String dasDat, String lastFertilizerDate) {
        fieldId.add(id);
        fieldName.add(name);
        fieldUnitArea.add(unitArea);
        fieldAreaValue.add(areaValue);
        fieldDasDat.add(dasDat);
        fieldLastDateFertilizer.add(lastFertilizerDate);

        notifyItemInserted(fieldId.size() - 1);
    }

    private void setAreaToSpinners(Spinner spin_fieldUnitArea,String rPosition){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.unitArea, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_fieldUnitArea.setAdapter(adapter);
        int position = adapter.getPosition(rPosition);
        spin_fieldUnitArea.setSelection(position);
    }

    private void showDeletionConfirmation(String fieldName, DeletionConfirmationListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Deleting")
                .setMessage("Are you sure you want to delete: " + fieldName + "?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onConfirm();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        listener.onCancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDatePickerDialog(TextView DATOrDASDate, TextView fertilizerApplicationDate) {
        final Calendar c = Calendar.getInstance();

        // Initialize DatePicker with the current date
        DatePicker datePicker = new DatePicker(context);
        datePicker.init(
                c.get(Calendar.YEAR),//get current year
                c.get(Calendar.MONTH),//get current month
                c.get(Calendar.DAY_OF_MONTH),//get current date
                null
        );

        // Create AlertDialog with the DatePicker as its view
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
}
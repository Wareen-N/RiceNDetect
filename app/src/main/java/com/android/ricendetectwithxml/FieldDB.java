package com.android.ricendetectwithxml;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class FieldDB extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "fieldDb";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "fieldTbl";
    private static final String COLUMN_FIELD_ID = "fieldId";
    private static final String COLUMN_FIELD_NAME = "fieldName";
    private static final String COLUMN_FIELD_UNIT_AREA = "fieldUnitArea";
    private static final String COLUMN_FIELD_AREA_VALUE = "fieldAreaValue";
    private static final String COLUMN_FIELD_DAT_DAS = "fieldDatDas";
    private static final String COLUMN_FIELD_LAST_DATE_FERTILIZER = "fieldsLastDateFertilizer";

    public FieldDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FIELD_NAME + " TEXT, " +
                COLUMN_FIELD_UNIT_AREA + " TEXT, " +
                COLUMN_FIELD_AREA_VALUE + " TEXT, " +
                COLUMN_FIELD_DAT_DAS + " TEXT, " +
                COLUMN_FIELD_LAST_DATE_FERTILIZER + " TEXT);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addFieldData(String fieldName, String fieldUnitArea, String fieldAreaValue, String fieldDatDas, String fieldLastDateFertilizer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FIELD_NAME, fieldName);
        cv.put(COLUMN_FIELD_UNIT_AREA, fieldUnitArea);
        cv.put(COLUMN_FIELD_AREA_VALUE, fieldAreaValue);
        cv.put(COLUMN_FIELD_DAT_DAS, fieldDatDas);
        cv.put(COLUMN_FIELD_LAST_DATE_FERTILIZER, fieldLastDateFertilizer);

        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed to add!", Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    public void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME,"fieldId=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Delete failed!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully deleted!", Toast.LENGTH_SHORT).show();
        }
    }

    public long updateFieldData(String fieldId, String fieldName, String fieldUnitArea, String fieldAreaValue, String fieldDatDas, String fieldLastDateFertilizer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FIELD_NAME, fieldName);
        cv.put(COLUMN_FIELD_UNIT_AREA, fieldUnitArea);
        cv.put(COLUMN_FIELD_AREA_VALUE, fieldAreaValue);
        cv.put(COLUMN_FIELD_DAT_DAS, fieldDatDas);
        cv.put(COLUMN_FIELD_LAST_DATE_FERTILIZER, fieldLastDateFertilizer);

        long result = db.update(TABLE_NAME, cv, "fieldId=?", new String[]{fieldId});
        if(result == -1){
            Toast.makeText(context, "Failed to update!", Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    Cursor readAllFieldData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

}

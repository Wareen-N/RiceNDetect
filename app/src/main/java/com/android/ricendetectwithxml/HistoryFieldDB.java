package com.android.ricendetectwithxml;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class HistoryFieldDB extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "historyFieldDb";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "historyFieldTbl";
    private static final String COLUMN_HISTORY_FIELD_ID = "historyFieldId";
    private static final String COLUMN_FIELD_ID = "fieldId";
    private static final String COLUMN_NITROGEN_LEVEL = "nitrogenLevel";
    private static final String COLUMN_FERTILIZER_AMOUNT = "nitrogenFertilizerAmount";
    private static final String COLUMN_DGCI_AVE = "DGCIAve";
    private static final String COLUMN_DATE = "Date";

    public HistoryFieldDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_HISTORY_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FIELD_ID + " TEXT, " +
                COLUMN_NITROGEN_LEVEL + " TEXT, " +
                COLUMN_FERTILIZER_AMOUNT + " TEXT, " +
                COLUMN_DGCI_AVE + " TEXT, " +
                COLUMN_DATE + " TEXT);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addHistoryInFieldData(String fieldId, String nitrogenLevel, String fertilizerAmount, String dgciAve, String dateHistory){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FIELD_ID, fieldId);
        cv.put(COLUMN_NITROGEN_LEVEL, nitrogenLevel);
        cv.put(COLUMN_FERTILIZER_AMOUNT, fertilizerAmount);
        cv.put(COLUMN_DGCI_AVE, dgciAve);
        cv.put(COLUMN_DATE, dateHistory);

        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed to add!", Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    public void deleteRowCorrespondsToFieldId(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME,"fieldId=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Delete failed!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully deleted!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readFieldHistoryDataAccordingToFieldId(String ID){
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_FIELD_ID + " = " + ID;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }


}

package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.AccessControlContext;


public class DBHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "200546G.db";

    // Contains the details regarding table 1
    private static final String TABLE_NAME1 = "account";
    private static final String ACCOUNT_NO_COL = "account_no";
    private static final String BANK_COL = "bank";
    private static final String ACC_HOLDER_COL = "acc_holder";
    private static final String BALANCE_COL = "balance";

    // Contains the details regarding table 2
    private static final String TABLE_NAME2 = "log";
    private static final String INDEX_COL = "id";
    private static final String DATE_COL = "date";
    private static final String TYPE_COL = "type";
    private static final String AMOUNT_COL = "amount";




    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String query1 = "CREATE TABLE " + TABLE_NAME1 + " ("
                + ACCOUNT_NO_COL + " TEXT PRIMARY KEY,"
                + BANK_COL + " TEXT,"
                + ACC_HOLDER_COL + " TEXT,"
                + BALANCE_COL + " REAL)";
        String query2 = "CREATE TABLE " + TABLE_NAME2 + " ("
                + INDEX_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DATE_COL + " TEXT,"
                + ACCOUNT_NO_COL + " TEXT,"
                + TYPE_COL + " TEXT,"
                + AMOUNT_COL + " REAL,"
                + "FOREIGN KEY (" + ACCOUNT_NO_COL + ") REFERENCES " + TABLE_NAME1 + "(" + ACCOUNT_NO_COL + "))";

        db.execSQL(query1);
        db.execSQL(query2);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        onCreate(db);
    }

}

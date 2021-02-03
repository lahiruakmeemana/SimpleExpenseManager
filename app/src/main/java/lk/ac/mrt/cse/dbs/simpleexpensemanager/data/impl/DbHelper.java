package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.Serializable;


public class DbHelper extends SQLiteOpenHelper implements Serializable{

    private static final String CREATE_ACCOUNT_ENTRIES = "CREATE TABLE Account_table ( Acc_no TEXT PRIMARY KEY, Bank TEXT, Holder TEXT, Balance REAL)";
    
    private static final String DELETE_ACCOUNT_ENTRIES = "DROP TABLE IF EXISTS Account_table";

    private static final String CREATE_TRANSACTION_ENTRIES = "CREATE TABLE Transaction_table ( Date TEXT PRIMARY KEY, Amount REAL, Type TEXT, Acc_no TEXT, FOREIGN KEY ( Acc_no) REFERENCES Account_table (Acc_no))";
            
    private static final String DELETE_TRANSACTION_ENTRIES = "DROP TABLE IF EXISTS Transaction_table";

    public DbHelper(Context context) {
        super(context, "180022T", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ACCOUNT_ENTRIES);
        db.execSQL(CREATE_TRANSACTION_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_ACCOUNT_ENTRIES);
        db.execSQL(DELETE_TRANSACTION_ENTRIES);
        onCreate(db);
    }
}
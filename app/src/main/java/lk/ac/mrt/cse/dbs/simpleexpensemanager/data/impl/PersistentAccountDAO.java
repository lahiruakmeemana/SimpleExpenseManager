package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


public class PersistanceAccountDAO implements AccountDAO,Serializable {

    private SQLiteOpenHelper dbHelper;

    public PersistanceAccountDAO(Context context) {
        dbHelper = new DbHelper(context);
    }

    @Override
    public List<String> getAccountNumbersList() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = String.format("SELECT %s FROM %s", "Acc_no", "Account_table");
                
        List<String> numbers = new ArrayList<>();
        final Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                numbers.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return numbers;
    }

    @Override
    public List<Account> getAccountsList() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = String.format("SELECT %s, %s, %s, %s FROM %s", "Acc_no", "Bank", "Holder", "Balance","Account_table");

        List<Account> accounts = new ArrayList<>();
        final Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                accounts.add(new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3)));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = String.format("SELECT %s, %s, %s, %s FROM %s WHERE %s = ?", "Acc_no", "Bank", "Holder", "Balance","Account_table", "Acc_no");
               
        final Cursor cursor = db.rawQuery(sql, new String[]{accountNo});
        cursor.moveToFirst();

        if (cursor.getCount()>0) {
            Account account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
            cursor.close();
            return account;
        }
        String msg = "Account " + accountNo + " is invalid.";
        cursor.close();
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = String.format("INSERT OR IGNORE INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)", "Account_table","Acc_no", "Bank", "Holder", "Balance");               
        db.execSQL(sql, new Object[]{account.getAccountNo(),account.getBankName(),account.getAccountHolderName(),account.getBalance()});
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        getAccount(accountNo);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = String.format("DELETE FROM %s WHERE %s = ?", "Account_table", "Acc_no");
        db.execSQL(sql, new Object[]{accountNo});
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        getAccount(accountNo);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = null;
        switch (expenseType) {
            case EXPENSE:
                sql = "UPDATE %s SET %s = %s - ? WHERE %s = ?";
                break;
            case INCOME:
                sql = "UPDATE %s SET %s = %s + ? WHERE %s = ?";
                break;
        }
        sql = String.format(sql, "Account_table", "Balance", "Balance", "Acc_no");
        db.execSQL(sql, new Object[]{amount, accountNo});
    }
}
package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DBHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private DBHandler dbHandler;

    public PersistentAccountDAO(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumberList = new ArrayList<>();

        SQLiteDatabase db = dbHandler.getReadableDatabase();

        String[] projection = {
                "account_no"
        };

        Cursor cursor = db.query(
                "account",
                projection,
                null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            accountNumberList.add(cursor.getString(0));
        }
        cursor.close();
        db.close();

        return accountNumberList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<>();

        SQLiteDatabase db = dbHandler.getReadableDatabase();

        String[] projection = {
                "account_no",
                "bank",
                "acc_holder",
                "balance"
        };

        Cursor cursor = db.query(
                "account",
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()) {
            String accountNumber = cursor.getString(0);
            String bankName = cursor.getString(1);
            String accountHolderName = cursor.getString(2);
            double balance = cursor.getDouble(3);

            Account account = new Account(accountNumber,bankName,accountHolderName,balance);
            accountList.add(account);

        }

        cursor.close();
        db.close();

        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHandler.getReadableDatabase();

        String[] projection = {
                "account_no",
                "bank",
                "acc_holder",
                "balance"
        };

        String selection = "account_no" + " = ?";
        String[] selectionArgs = {accountNo};

        Cursor cursor = db.query(
                "account",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        cursor.moveToNext();

        String accountNumber = cursor.getString(0);
        String bankName = cursor.getString(1);
        String accountHolderName = cursor.getString(2);
        double balance = cursor.getDouble(3);

        Account account = new Account(accountNumber,bankName,accountHolderName,balance);


        cursor.close();
        db.close();

        return account;
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();

        String accountNumber = account.getAccountNo();
        String bankName = account.getBankName();
        String accountHolder = account.getAccountHolderName();
        double balance = account.getBalance();

        values.put("account_no", accountNumber);
        values.put("bank", bankName);
        values.put("acc_holder", accountHolder);
        values.put("balance", balance);

        db.insert("account", null, values);

        db.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        String selection = "account_no" + "LIKE ?";
        String[] selectionArgs = {accountNo};
        int deleteRows = db.delete("account", selection, selectionArgs);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        String[] projection = {
                "balance"
        };

        String selection = "account_no" + " LIKE ?";
        String[] selectionArgs = {accountNo};

        Cursor cursor = db.query(
                "account",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        cursor.moveToNext();
        double balance = cursor.getDouble(0);
        cursor.close();

        ContentValues values = new ContentValues();
        switch (expenseType) {
            case EXPENSE:
                balance = balance - amount;
            case INCOME:
                balance = balance + amount;
        }
        values.put("balance", balance);

        int count = db.update(
                "account",
                values,
                selection,
                selectionArgs
        );
    }
}

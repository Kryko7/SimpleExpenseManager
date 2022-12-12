package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.db.DBHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO  implements TransactionDAO {
    private DBHandler dbHandler;

    public PersistentTransactionDAO(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();

        String type = "";
        if(expenseType == ExpenseType.INCOME) {
            type = "income";
        } else {
            type = "expense";
        }
        long currentDate = date.getTime();

        values.put("date", currentDate);
        values.put("account_no", accountNo);
        values.put("type", type);
        values.put("amount", amount);

        db.insert("log", null, values);

        db.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = dbHandler.getReadableDatabase();

        String[] projection = {
                "date",
                "account_no",
                "type",
                "amount"
        };

        Cursor cursor = db.query(
                "log",
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            Date date = new Date(cursor.getLong(0));
            String accountNumber = cursor.getString(1);
            String type = cursor.getString(2);
            ExpenseType expenseType = null;
            if(type.equals("expense")) {
                expenseType = ExpenseType.EXPENSE;
            } else {
                expenseType = ExpenseType.INCOME;
            }
            double amount = cursor.getDouble(3);
            Transaction transaction = new Transaction(date, accountNumber, expenseType, amount);

            transactionList.add(transaction);
        }
        cursor.close();
        db.close();

        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        List<Transaction> transactionList = getAllTransactionLogs();
        int size = transactionList.size();
        if (size <= limit) {
            return  transactionList;
        }
        return  transactionList.subList(size - limit, size);
    }
}

package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistanceAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistanceTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class PersistanceExpenseManager extends ExpenseManager{

    private transient Context context;

    public PersistanceExpenseManager(Context context) {
        this.context = context;
        setup();
    }

    @Override
    public void setup() {
        
        TransactionDAO PersistanceTransactionDAO = new PersistanceTransactionDAO(context);
        setTransactionsDAO(PersistanceTransactionDAO);

        AccountDAO PersistanceAccountDAO = new PersistanceAccountDAO(context);
        setAccountsDAO(PersistanceAccountDAO);

        // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);

        
    }
}
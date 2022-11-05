package ng.pencode.moneytrackerv2;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ExpensesRepository {
    private ExpensesDao expensesDao;
    private LiveData<List<Expenses>> allExpenses;

    public ExpensesRepository(Application application){
        ExpensesDatabase database = ExpensesDatabase.getInstance(application);
        expensesDao = database.expensesDao();
        allExpenses = expensesDao.getAllExpenses();
    }

    /*crud operation implementation*/
    public void insert(Expenses expenses){
        new InsertExpensesAsyncTask(expensesDao).execute(expenses);
    }

    public void update(Expenses expenses){
        new UpdateExpensesAsyncTask(expensesDao).execute(expenses);
    }

    public void delete(Expenses expenses){
        new DeleteExpensesAsyncTask(expensesDao).execute(expenses);
    }

    public void deleteAllExpenses(){
        new DeleteAllExpensesAsyncTask(expensesDao).execute();
    }

    public LiveData<List<Expenses>> getAllExpenses() {
        return allExpenses;
    }

    private static class InsertExpensesAsyncTask extends AsyncTask<Expenses, Void, Void>{
        private ExpensesDao expensesDao;

        private InsertExpensesAsyncTask(ExpensesDao expensesDao){
            this.expensesDao = expensesDao;
        }
        @Override
        protected Void doInBackground(Expenses... expenses) {
            expensesDao.insert(expenses[0]);
            return null;
        }
    }

    private static class UpdateExpensesAsyncTask extends AsyncTask<Expenses, Void, Void>{
        private ExpensesDao expensesDao;

        private UpdateExpensesAsyncTask(ExpensesDao expensesDao){
            this.expensesDao = expensesDao;
        }
        @Override
        protected Void doInBackground(Expenses... expenses) {
            expensesDao.update(expenses[0]);
            return null;
        }
    }

    private static class DeleteExpensesAsyncTask extends AsyncTask<Expenses, Void, Void>{
        private ExpensesDao expensesDao;

        private DeleteExpensesAsyncTask(ExpensesDao expensesDao){
            this.expensesDao = expensesDao;
        }
        @Override
        protected Void doInBackground(Expenses... expenses) {
            expensesDao.delete(expenses[0]);
            return null;
        }
    }

    private static class DeleteAllExpensesAsyncTask extends AsyncTask<Void, Void, Void>{
        private ExpensesDao expensesDao;

        private DeleteAllExpensesAsyncTask(ExpensesDao expensesDao){
            this.expensesDao = expensesDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            expensesDao.deleteAllExpenses();
            return null;
        }
    }
}

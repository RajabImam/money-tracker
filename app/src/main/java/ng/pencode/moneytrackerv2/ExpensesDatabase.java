package ng.pencode.moneytrackerv2;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

//version specify for migration purpose and when schema changes
@Database(entities = {Expenses.class}, version = 1)
public abstract  class ExpensesDatabase extends RoomDatabase {
    private static ExpensesDatabase instance;

    //To access our Dao
    public abstract ExpensesDao expensesDao();

    //singleton to get instance, only one thread access at a time
    public static synchronized ExpensesDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ExpensesDatabase.class, "expenses_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };
    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void>{
        ExpensesDao expensesDao;
        //Just populating sample data for the first time the database is created and happens only once
        private PopulateDBAsyncTask(ExpensesDatabase db){
            expensesDao = db.expensesDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            expensesDao.insert(new Expenses("Food", "Rice", 10.0, "04/11/2022"));
            expensesDao.insert(new Expenses("Transportation", "Train", 3.8, "04/11/2022"));
            expensesDao.insert(new Expenses("Entertainment", "Netflix", 10.0, "04/11/2022"));
            return null;
        }
    }
}

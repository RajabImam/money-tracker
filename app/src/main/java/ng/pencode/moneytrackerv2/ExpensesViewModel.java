package ng.pencode.moneytrackerv2;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ExpensesViewModel extends AndroidViewModel {
    private ExpensesRepository repository;
    private LiveData<List<Expenses>> allExpenses;

    public ExpensesViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpensesRepository(application);
        allExpenses = repository.getAllExpenses();
    }

    /*Database operations*/
    public void insert(Expenses expenses){
        repository.insert(expenses);
    }

    public void update(Expenses expenses){
        repository.update(expenses);
    }

    public void delete(Expenses expenses){
        repository.delete(expenses);
    }

    public void deleteAllExpenses(){
        repository.deleteAllExpenses();
    }

    public LiveData<List<Expenses>> getAllExpenses() {
        return allExpenses;
    }
}

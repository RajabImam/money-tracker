package ng.pencode.moneytrackerv2;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExpensesDao {
    /*Data Access Objects All methods for the crud operation*/

    @Insert
    void insert(Expenses expenses);

    @Update
    void update(Expenses expenses);

    @Delete
    void delete(Expenses expenses);

    @Query("DELETE FROM expenses_table")
    void deleteAllExpenses();

    @Query("SELECT * FROM expenses_table ORDER BY date ASC")
    LiveData<List<Expenses>> getAllExpenses();

}

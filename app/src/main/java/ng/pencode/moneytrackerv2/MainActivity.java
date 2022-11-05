package ng.pencode.moneytrackerv2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_EXPENSES_REQUEST = 1;
    public static final int EDIT_EXPENSES_REQUEST = 2;
    private ExpensesViewModel expensesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddExpenses = findViewById(R.id.btn_add_expenses);
        buttonAddExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditExpensesActivity.class);
                startActivityForResult(intent, ADD_EXPENSES_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        ExpensesAdapter adapter = new ExpensesAdapter();
        recyclerView.setAdapter(adapter);

        //getting view model instance
        expensesViewModel = ViewModelProviders.of(this).get(ExpensesViewModel.class);
        expensesViewModel.getAllExpenses().observe(this, new Observer<List<Expenses>>() {
            @Override
            public void onChanged(List<Expenses> expenses) {
                //trigger every time our live data object changes
                //update RecycleView
                adapter.submitList(expenses);
            }
        });

        //Swipe able method to support left and right swipe
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //get to position to delete
                expensesViewModel.delete(adapter.getExpensesAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Expenses Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new ExpensesAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Expenses expenses) {
                //Open the activity to edit expenses
                Intent intent = new Intent(MainActivity.this, AddEditExpensesActivity.class);
                intent.putExtra(AddEditExpensesActivity.EXTRA_ID, expenses.getId());
                intent.putExtra(AddEditExpensesActivity.EXTRA_TITLE, expenses.getTitle());
                intent.putExtra(AddEditExpensesActivity.EXTRA_DESCRIPTION, expenses.getDescription());
                intent.putExtra(AddEditExpensesActivity.EXTRA_AMOUNT, expenses.getAmount());
                intent.putExtra(AddEditExpensesActivity.EXTRA_DATE, expenses.getDate());
                startActivityForResult(intent, EDIT_EXPENSES_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EXPENSES_REQUEST && resultCode == RESULT_OK){
            String title = data.getStringExtra(AddEditExpensesActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditExpensesActivity.EXTRA_DESCRIPTION);
            Double amount = data.getDoubleExtra(AddEditExpensesActivity.EXTRA_AMOUNT, 0.0);
            String date = data.getStringExtra(AddEditExpensesActivity.EXTRA_DATE);

            Expenses expenses = new Expenses(title, description, amount, date);
            expensesViewModel.insert(expenses);

            Toast.makeText(this, "Expenses Successfully Saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_EXPENSES_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditExpensesActivity.EXTRA_ID, -1);
            //check to be sure
            if (id == -1){
                Toast.makeText(this, "Expenses cannot be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddEditExpensesActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditExpensesActivity.EXTRA_DESCRIPTION);
            Double amount = data.getDoubleExtra(AddEditExpensesActivity.EXTRA_AMOUNT, 0.0);
            String date = data.getStringExtra(AddEditExpensesActivity.EXTRA_DATE);

            Expenses expenses = new Expenses(title, description, amount, date);
            expenses.setId(id);
            expensesViewModel.update(expenses);
            Toast.makeText(this, "Expenses Updated Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Not Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_expenses:
              expensesViewModel.deleteAllExpenses();
                Toast.makeText(this, "All Expenses Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
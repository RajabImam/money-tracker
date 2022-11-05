package ng.pencode.moneytrackerv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.autofill.AutofillValue;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditExpensesActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "ng.pencode.moneytrackerv2.EXTRA_ID";
    public static final String EXTRA_TITLE = "ng.pencode.moneytrackerv2.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "ng.pencode.moneytrackerv2.EXTRA_DESCRIPTION";
    public static final String EXTRA_AMOUNT = "ng.pencode.moneytrackerv2.EXTRA_AMOUNT";
    public static final String EXTRA_DATE = "ng.pencode.moneytrackerv2.EXTRA_DATE";

    //fields variable for the widget
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextAmount;
    private EditText datePicker;
    Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses);

        editTextTitle = findViewById(R.id.edit_title);
        editTextDescription = findViewById(R.id.edit_description);
        editTextAmount = findViewById(R.id.edit_amount);
        datePicker = findViewById(R.id.date_picker_date);
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateCalendar();
            }

            private void updateCalendar(){
                String format = "MM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.FRANCE);
                datePicker.setText(sdf.format(calendar.getTime()));
            }
        };
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddEditExpensesActivity.this, date, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        //check to set appropriate title and view to add and to update
        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Update Expenses");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            //editTextAmount.setText(intent.getStringExtra(EXTRA_AMOUNT));
            editTextAmount.setText(String.valueOf(intent.getDoubleExtra(EXTRA_AMOUNT, 0.0)));
            datePicker.autofill(AutofillValue.forText(intent.getStringExtra(EXTRA_DATE)));
        }else{
            setTitle("Add Expenses");
        }

    }

    //to save the expenses
    private void saveExpenses(){
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        Double amount = Double.valueOf(editTextAmount.getText().toString());
        String date = datePicker.getText().toString();

        //Field Validation
        if (title.trim().isEmpty() || description.trim().isEmpty() || date.trim().isEmpty()){
            Toast.makeText(this, "Fields are required", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_AMOUNT, amount);
        data.putExtra(EXTRA_DATE, date);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        //the only way we put id because no entry with -1
        if (id != -1){
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_expenses_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_expenses:
                saveExpenses();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
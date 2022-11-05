package ng.pencode.moneytrackerv2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.util.Date;
import java.util.Formatter;

@Entity(tableName = "expenses_table")
public class Expenses {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private double amount;
    private String date;

    public Expenses(String title, String description, double amount, String date) {
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

}

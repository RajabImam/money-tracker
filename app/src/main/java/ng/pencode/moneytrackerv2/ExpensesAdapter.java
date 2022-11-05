package ng.pencode.moneytrackerv2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExpensesAdapter extends ListAdapter<Expenses, ExpensesAdapter.ExpensesHolder> {
    //private List<Expenses> expenses = new ArrayList<>();
    private onItemClickListener listener;

    public ExpensesAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Expenses> DIFF_CALLBACK = new DiffUtil.ItemCallback<Expenses>() {
        @Override
        public boolean areItemsTheSame(@NonNull Expenses oldItem, @NonNull Expenses newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Expenses oldItem, @NonNull Expenses newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getAmount() == newItem.getAmount() &&
                    oldItem.getDate().equals(newItem.getDate());
        }
    };

    @NonNull
    @Override
    public ExpensesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expenses_item, parent, false);
        return new ExpensesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpensesHolder holder, int position) {
        Expenses currentExpenses = getItem(position);
        holder.textViewTitle.setText(currentExpenses.getTitle());
        holder.textViewDescription.setText(currentExpenses.getDescription());
        holder.textViewAmount.setText(String.valueOf(currentExpenses.getAmount()));
        holder.textViewDate.setText(currentExpenses.getDate());
    }

   /* @Override
    public int getItemCount() {
        return expenses.size();
    }

    public void setExpenses(List<Expenses> expenses){
        this.expenses = expenses;
        notifyDataSetChanged();
    }*/

    //get the expenses position to the outside world
    public Expenses getExpensesAt(int position){
        return getItem(position);
    }

    class ExpensesHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewAmount;
        private TextView textViewDate;

        public ExpensesHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.txt_title);
            textViewDescription = itemView.findViewById(R.id.txt_description);
            textViewAmount = itemView.findViewById(R.id.txt_amount);
            textViewDate = itemView.findViewById(R.id.txt_date);
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    //Check to not click an invalid position
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    //interface to make expenses clickable before editing
    public interface onItemClickListener{
        void onItemClick(Expenses expenses);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }
}

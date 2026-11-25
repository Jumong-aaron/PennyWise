import java.util.ArrayList;
import java.util.List;

/** Manages the collection of expense records. */
public class ExpensesManager {
    private final ArrayList<Expenses> expenses = new ArrayList<>();

    public void addExpense(Expenses expense) {
        expenses.add(expense);
    }

    /** Removes the expense at the given position. Returns false if invalid. */
    public boolean deleteExpense(int index) {
        if (index < 0 || index >= expenses.size()) {
            return false;
        }
        expenses.remove(index);
        return true;
    }

    /** Total of all expenses. */
    public double calculateTotal() {
      double total = 0;
      for (Expenses e : expenses) {
        total += e.getAmount();
      }
      return total;
    }

    /**
     * Total of one category. "Others" also includes any record whose
     * category is not Food, Transportation or School.
     */
    public double calculateTotal(String category) {
        double total = 0;
        for (Expenses e : expenses) {
            String cat = e.getCategory() == null ? "" : e.getCategory();
            boolean known = cat.equals("Food") || cat.equals("Transportation")
                    || cat.equals("School");
            boolean match = category.equals("Others")
                    ? !known : cat.equals(category);
            if (match) {
                total += e.getAmount();
            }
        }
        return total;
    }

    public ArrayList<Expenses> getExpenses() {
        return new ArrayList<>(expenses);
    }

    /** Replaces the whole collection (used after loading from the file). */
    public void setExpenses(List<Expenses> loaded) {
        expenses.clear();
        expenses.addAll(loaded);
    }
}

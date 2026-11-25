import java.util.ArrayList;
import java.util.List;

public class ExpensesManager {
    private final ArrayList<Expenses> expenses = new ArrayList<>();

    public void addExpense(Expenses expense) {
        expenses.add(expense);
    }

    public boolean deleteExpense(int index) {
        if (index < 0 || index >= expenses.size()) {
            return false;
        }
        expenses.remove(index);
        return true;
    }

    public double calculateTotal() {
      double total = 0;
      for (Expenses e : expenses) {
        total += e.getAmount();
      }
      return total;
    }

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
    public void setExpenses(List<Expenses> loaded) {
        expenses.clear();
        expenses.addAll(loaded);
    }
}

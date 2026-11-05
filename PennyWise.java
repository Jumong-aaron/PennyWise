import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.arrayList;


class Expense{
	private String description;
	private double amount;
	private string category;
	private LocalDate date;

	public Expense(String description, double amount, String category, LocalDate date;) {
		this.description = description;
		this.amount = amount;
		this.category = category;
		this.date = date;
	
	}

	public String getDescription() {
	return description;
	}
	
	public double getAmount() {
		return amount;
	}
	public String getCategory() {
	return category;
	}
	public LocalDate getDate() {
	return date;
	}
}

	
class ExpensesManager {
	private ArrayList<Expense> expenses = new ArrayList<>();

	public void addExpense(Expense expense) {
		expenses.add(expense);
	}

	public void deleteExpense(int index) {
		if (index >= 0 && index < expenses.size()) {
			expenses.remove(index);
		}
	}
	public double calculateTotal() {
		double total = 0;

		for (Expense expense : expenses) {
			total += expense.getAmount();
		}

		return total;
	}

	public ArrayList<Expense> getExpenses() {
		return expenses;
	}
}


class FilManager{
	private String fileName= "expenses.txt";

	private void saveExpenses(Arraylist<Expense> expenses) {
		try (PrintWriter writer = new PrintWriter((new FileWriter(fileName))) {
			
			 for (Expense expense : expense) {
				 writer.println(
					 expense.getDescription() + "|" +
					 expense.getAmount() + "|" +
					 expense.getCategpry() + "|" +
				 )
			 }
		} catch (IOException e) {
			JOptionpane.showMessageDialog(
				null, "Error saving expenses: " + e,getMessage()
			);
		}
	}

	piblic ArrayList<Expense> loadExpenses() {
		ArrayList<Expense> expenses = new ArrayList<>();
		File file = new File(fileName);

		if(!file.exists()) {
			return expenses;
		}

		try (BufferedReader reader =  new BufferedReader(new FileReader(fileName))) {
			
			String line;

			while((line = reader.readLine()) != null) {
				String [] data = line.split("\\|", -1);

				if (data.length == 4) {
					try{
						String description = data[0];
						double amount = Double.parseDouble(data[1]);
						String category = data[2];
						LocalDate date = LocalDate.parse(dat[3]);

						expenses.add(new Expense (description, amount, category, data));
					} catch (IllegatArgumentException e) {
						System.out.println("Skiping invalid expense record.")
					}
				}
			} catch (IOException e){
				JOptionPane.showMessageDialog(null, "Error loading expense: " + e.getMessage());
			}
			return expenses;
		}
	}
	

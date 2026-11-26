import java.time.LocalDate;

public class Expenses {
  private final String description;
  private final double amount;
  private final String category;
  private final LocalDate date;

  public Expenses(String description, double amount, String category, LocalDate date;) {
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

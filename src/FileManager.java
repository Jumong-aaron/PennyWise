import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private final String fileName;
    private final String goalFileName;

    public FileManager(String fileName, String goalFileName) {
        this.fileName = fileName;
        this.goalFileName = goalFileName;
    }

    public void saveExpenses(List<Expenses> expenses) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Expenses e : expenses) {
            lines.add(Utils.clean(e.getDescription()) + "\t" + e.getAmount()
                    + "\t" + e.getCategory() + "\t" + e.getDate());
        }
        createFolderFor(fileName);
        Files.write(Paths.get(fileName), lines, StandardCharsets.UTF_8);
    }

    public List<Expenses> loadExpenses() {
        List<Expenses> result = new ArrayList<>();
        try {
            if (Files.exists(Paths.get(fileName))) {
                for (String line : Files.readAllLines(
                        Paths.get(fileName), StandardCharsets.UTF_8)) {
                    String[] p = line.split("\t");
                    if (p.length < 4) {
                        continue;
                    }
                    try {
                        result.add(new Expenses(p[0],
                                Double.parseDouble(p[1]), p[2],
                                LocalDate.parse(p[3])));
                    } catch (RuntimeException bad) {
                        // skip a damaged line
                    }
                }
            }
        } catch (IOException ex) {
           
        }
        return result;
    }

    public void saveGoal(String name, double target, double saved)
            throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add(Utils.clean(name));
        lines.add(String.valueOf(target));
        lines.add(String.valueOf(saved));
        createFolderFor(goalFileName);
        Files.write(Paths.get(goalFileName), lines, StandardCharsets.UTF_8);
    }

    public SavingsGoal loadGoal() {
        SavingsGoal goal = new SavingsGoal();
        try {
            if (Files.exists(Paths.get(goalFileName))) {
                List<String> lines = Files.readAllLines(
                        Paths.get(goalFileName), StandardCharsets.UTF_8);
                if (lines.size() >= 3) {
                    goal.name = lines.get(0);
                    goal.target = Double.parseDouble(lines.get(1));
                    goal.saved = Double.parseDouble(lines.get(2));
                }
            }
        } catch (IOException | RuntimeException ex) {
            goal = new SavingsGoal();
        }
        return goal;
    }

    private void createFolderFor(String file) throws IOException {
        Path parent = Paths.get(file).getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }
}


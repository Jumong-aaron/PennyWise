import java.util.Locale;


public final class Utils {

    private Utils() {
    }

    public static String clean(String text) {
        return text == null ? "" : text.replaceAll("[\\t\\r\\n]+", " ").trim();
    }

    public static Double parseMoney(String text) {
        if (text == null) {
            return null;
        }
        String t = text.replace(",", "").replace(Theme.PESO, "").trim();
        if (!t.matches("\\d{1,12}(\\.\\d{1,2})?")) {
            return null;
        }
        double value = Double.parseDouble(t);
        return value > 0 ? Double.valueOf(value) : null;
    }

    public static String peso(double value) {
        return Theme.PESO + " " + String.format(Locale.US, "%,.2f", value);
    }
}

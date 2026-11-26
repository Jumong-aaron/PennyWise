import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


public class RowRenderer extends DefaultTableCellRenderer {
    private final int align;

    public RowRenderer(int align) {
        this.align = align;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value,
                isSelected, false, row, column);
        setHorizontalAlignment(align);
        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        if (isSelected) {
            setBackground(new Color(190, 232, 205));
            setForeground(Theme.DARK_GREEN);
        } else {
            setBackground(row % 2 == 0 ? Color.WHITE
                    : new Color(245, 251, 248));
            setForeground(new Color(30, 70, 60));
        }
        return c;
    }
}

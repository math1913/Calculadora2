import javax.swing.*;
import java.awt.*;

public class Estetica {

    public static JTextField createDisplay() {
        JTextField d = new JTextField("");
        d.setFont(Theme.FONT_DISPLAY);
        d.setHorizontalAlignment(SwingConstants.RIGHT);
        d.setEditable(false);
        d.setBackground(Theme.BG_DISPLAY);
        d.setForeground(Theme.FG_DISPLAY);
        d.setBorder(BorderFactory.createEmptyBorder(
            Theme.PADDING_DISPLAY,
            Theme.PADDING_DISPLAY,
            Theme.PADDING_DISPLAY,
            Theme.PADDING_DISPLAY
        ));
        return d;
    }

    public static JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.FONT_BTN);
        btn.setForeground(Theme.FG_BTN);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        return btn;
    }

    public static void styleAsNumber(JButton btn) {
        btn.setBackground(Theme.BG_BTN_NUM);
    }

    public static void styleAsOperator(JButton btn) {
        btn.setBackground(Theme.BG_BTN_OP);
    }

    public static void styleAsFunction(JButton btn) {
        btn.setBackground(Theme.BG_BTN_FUNC);
    }

    public static JPanel createButtonGrid(int rows, int cols) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Theme.BG_MAIN);
        return p;
    }

    public static JPanel createMainPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.BG_MAIN);
        return p;
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class CalculatorFrame extends JFrame {

    private final JTextField display;
    private final CalculatorState state;
    private final History history;
    private boolean resetOnNextDigit = false;

    public CalculatorFrame() {
        super("Calculadora per la Marineta");

        state   = new CalculatorState();
        history = new History();
        //ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);

        JPanel content = Estetica.createMainPanel();
        setContentPane(content);

        display = Estetica.createDisplay();
        content.add(display, BorderLayout.NORTH);

        JPanel buttonPanel = Estetica.createButtonGrid(6, 4);
        GridLayout grid4 = new GridLayout(6, 4, Theme.GAP, Theme.GAP);
        buttonPanel.setLayout(grid4);

        String[] buttons = {
            "Historial", "Limpiar", "AC", "←",
            "1/x", "x²", "sqrt(x)", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            ".", "0", "±", "="
        };
        for (String text : buttons) {
            JButton btn = Estetica.createButton(text);
            // asignar color según tipo
            if ("0123456789".contains(text))
                Estetica.styleAsNumber(btn);
            else if ("±".equals(text) || ".".equals(text) || "1/x".equals(text) || "x²".equals(text) || "sqrt(x)".equals(text))
                Estetica.styleAsFunction(btn);
            else if ("=".equals(text))
                Estetica.styleAsOperator(btn);
            else
                Estetica.styleAsOperator(btn);
            btn.addActionListener(new ButtonClickListener());
            buttonPanel.add(btn);
        }
        content.add(buttonPanel, BorderLayout.CENTER);

        // ─── Key Bindings para teclado ─────────────────────────────────────────────
        JRootPane root = getRootPane();
        // Dígitos
        for (char c = '0'; c <= '9'; c++) {
            String k = String.valueOf(c);
            root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(c), k);
            root.getActionMap().put(k, new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    new ButtonClickListener().actionPerformed(
                        new ActionEvent(this, 0, k));
                }
            });
        }
        // Punto
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke('.', 0), ".");
        root.getActionMap().put(".", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                new ButtonClickListener().actionPerformed(
                    new ActionEvent(this, 0, "."));
            }
        });
        // Operadores
        for (String op : new String[]{"+", "-", "*", "/"}) {
            root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(op.charAt(0)), op);
            root.getActionMap().put(op, new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    new ButtonClickListener().actionPerformed(
                        new ActionEvent(this, 0, op));
                }
            });
        }
        // Enter → =
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "=");
        root.getActionMap().put("=", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                new ButtonClickListener().actionPerformed(
                    new ActionEvent(this, 0, "="));
            }
        });
        // Backspace → ←
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "←");
        root.getActionMap().put("←", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                new ButtonClickListener().actionPerformed(
                    new ActionEvent(this, 0, "←"));
                }
            });
        // Esc → AC
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "AC");
        root.getActionMap().put("AC", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                new ButtonClickListener().actionPerformed(
                    new ActionEvent(this, 0, "AC"));
                }
            });
        setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            // Dígitos
            if ("0123456789".contains(cmd)) {
                if (resetOnNextDigit || "".equals(display.getText())) {
                    display.setText(cmd);
                } else {
                    display.setText(display.getText() + cmd);
                }
                resetOnNextDigit = false;
            // .
            } else if (".".equals(cmd)) {
                if (resetOnNextDigit) {
                    display.setText("0.");
                    resetOnNextDigit = false;
                    return;
                }
                String t = display.getText();
                int last = t.lastIndexOf(' ');
                String cur = last < 0 ? t : t.substring(last + 1);
                if (!cur.contains(".")) {
                    display.setText(t + ".");
                }

            // op
            } else if ("+-*/".contains(cmd)) {
                String txt = display.getText().trim();
                String[] parts = txt.split(" ");

                if (parts.length == 3) {
                    double r = state.calculate(Double.parseDouble(parts[2]));
                    display.setText(r + " " + cmd + " ");
                    state.setAccumulator(r);

                } else if (parts.length == 2) {
                    display.setText(parts[0] + " " + cmd + " ");

                } else {
                    state.setAccumulator(Double.parseDouble(parts[0]));
                    display.setText(parts[0] + " " + cmd + " ");
                }
                state.setOperator(cmd);
                resetOnNextDigit = false;

            // =
            } else if ("=".equals(cmd)) {
                String txt = display.getText().trim();
                String[] parts = txt.split(" ");
                if (parts.length == 3 && state.hasPendingOperator()) {
                    double right = Double.parseDouble(parts[2]);
                    double res = state.calculate(right);
                    history.agregarOperacion(txt + " = " + res);
                    display.setText(String.valueOf(res));
                    state.setAccumulator(res);
                    state.clearOperator();
                    resetOnNextDigit = true;
                } else if (parts.length == 1 && state.canRepeatLastOp()) {
                    double cur = Double.parseDouble(parts[0]);
                    double res = state.calculate(cur);
                    String op = state.getLastOp();
                    double operand = state.getLastOperand();
                    history.agregarOperacion(parts[0] + " " + op + " " + operand + " = " + res);
                    display.setText(String.valueOf(res));
                    state.setAccumulator(res);
                    resetOnNextDigit = true;
                }

            // signo
            } else if ("±".equals(cmd)) {
                double v = Double.parseDouble(display.getText());
                display.setText(String.valueOf(Aritmetica.signo(v)));
            }else if ("Historial".equals(cmd)) {
                StringBuilder sb = new StringBuilder("Historial de operaciones:\n");
                for (String op : history.obtenerOperaciones()) {
                    sb.append(op).append("\n");
                }
                JOptionPane.showMessageDialog(
                    CalculatorFrame.this,
                    sb.toString(),
                    "Historial",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else if ("Limpiar".equals(cmd)) {
                int res = JOptionPane.showConfirmDialog(
                    CalculatorFrame.this,
                    "¿Borrar todo el historial?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
                );
                if (res == JOptionPane.YES_OPTION) {
                    history.borrarHistorial();
                    JOptionPane.showMessageDialog(
                        CalculatorFrame.this,
                        "Historial borrado.",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            } else if ("AC".equals(cmd)) {
                display.setText("");
                state.reset();
                resetOnNextDigit = true;
            } else if ("←".equals(cmd)) {
                String txt = display.getText();
                if (txt.contains(" ")) {
                    int idx = txt.lastIndexOf(' ');
                    display.setText(txt.substring(0, idx + 3)); //conserva "número op "
                } else if (txt.length() > 1) {
                    display.setText(txt.substring(0, txt.length() - 1));
                } else {
                    display.setText("");
                }
            }else if ("1/x".equals(cmd)){
                double v=  Double.parseDouble(display.getText());
                if (v == 0)
                    display.setText("Undefined");
                else{
                    double res = Aritmetica.unoDiv(v);
                    display.setText(String.valueOf(res));
                    history.agregarOperacion("1/" + v + " = " + res);
                }
            }else if ("x²".equals(cmd)){
                double v =  Double.parseDouble(display.getText());
                double res = Aritmetica.multiplicar(v,v);
                display.setText(String.valueOf(res));
                history.agregarOperacion(v + "²" + " = " + res);
            }else if ("sqrt(x)".equals(cmd)){
                double v=  Double.parseDouble(display.getText());
                double res = Aritmetica.raiz(v);
                display.setText(String.valueOf(res));
                history.agregarOperacion("sqrt(" + v + ")" + " = " + res);
            }
        }
    }
}

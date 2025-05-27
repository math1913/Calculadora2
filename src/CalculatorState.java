public class CalculatorState {
    private double accumulator = 0;
    private String pendingOp = null;

    //para repetir operación
    private String lastOp = null;
    private double lastOperand = 0;

    public void setAccumulator(double value) {
        this.accumulator = value;
    }

    public void setOperator(String op) {
        this.pendingOp = op;
    }

    public boolean hasPendingOperator() {
        return pendingOp != null;
    }

    public double calculate(double currentDisplay) {
        if (pendingOp!= null){
            switch (pendingOp) {
                case "+": accumulator = Aritmetica.suma(accumulator, currentDisplay); break;
                case "-": accumulator = Aritmetica.resta(accumulator, currentDisplay); break;
                case "*": accumulator = Aritmetica.multiplicar(accumulator, currentDisplay); break;
                case "/": accumulator = Aritmetica.dividir(accumulator, currentDisplay); break;
            }
            //por si repite
            lastOp = pendingOp;
            lastOperand = currentDisplay;
            pendingOp = null;
        }else if (lastOp != null){ //repite con el ultimo
            switch (lastOp) {
            case "+": accumulator = Aritmetica.suma(accumulator, lastOperand); break;
            case "-": accumulator = Aritmetica.resta(accumulator, lastOperand); break;
            case "*": accumulator = Aritmetica.multiplicar(accumulator, lastOperand); break;
            case "/": accumulator = Aritmetica.dividir(accumulator, lastOperand); break;
            }
        }
        return accumulator;
    }

    public boolean canRepeatLastOp() {return lastOp != null;}
    public String getLastOp() { return lastOp; }
    public double getLastOperand() { return lastOperand; }

    public void reset() {
        accumulator = 0;
        pendingOp = null;
        lastOp = null;
        lastOperand = 0;
    }

    public double getAccumulator() {
        return accumulator;
    }

    public void clearOperator() {
        pendingOp = null;
    }
}

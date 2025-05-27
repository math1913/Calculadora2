public class Aritmetica {

    public static double suma(double a, double b) {
        return a + b;
    }

    public static double resta(double a, double b) {
        return a - b;
    }

    public static double multiplicar(double a, double b) {
        return a * b;
    }

    public static double dividir(double a, double b) throws ArithmeticException {
        if (b == 0) throw new ArithmeticException("División entre cero");
        return a / b;
    }
    
    public static double signo(double a) {
        return -a;
    }

    public static double unoDiv(double a) {
        if (a == 0) throw new ArithmeticException("División entre cero");
        return 1/a;
    }

    public static double raiz(double a) {
        return Math.sqrt(a);
    }
}

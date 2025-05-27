
public class Borrar {

    // Borra el último carácter del string, si hay alguno
    public static String backspace(String input) {
        if (input != null && input.length() > 0) {
            return input.substring(0, input.length() - 1); //devuelve un sub string del primero al penultimo
        }
        return "";
    }

    // Limpia el input completo
    public static String cls() {
        return "";
    }
}

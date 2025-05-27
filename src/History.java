import java.util.ArrayList;
import java.util.List;

public class History {
    private final List<String> operaciones = new ArrayList<>();

    public void agregarOperacion(String operacion) {
        operaciones.add(operacion);
    }

    public List<String> obtenerOperaciones() {
        return new ArrayList<>(operaciones); // Devolver copia
    }

    public void limpiar() {
        operaciones.clear();
    }
    public void borrarHistorial() {
        operaciones.clear();
    }
}

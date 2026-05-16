package excepciones;

/**
 * Excepción personalizada para representar errores ocurridos en la capa
 * de persistencia.
 *
 * Esta excepción permite encapsular errores técnicos de MongoDB, conversiones
 * inválidas o problemas al acceder a la base de datos.
 */
public class PersistenciaException extends Exception {

    public PersistenciaException(String mensaje) {
        super(mensaje);
    }

    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
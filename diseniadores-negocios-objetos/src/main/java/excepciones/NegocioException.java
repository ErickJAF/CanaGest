package excepciones;

/**
 * Excepción personalizada para representar errores ocurridos en la capa
 * de persistencia.
 *
 * Esta excepción permite encapsular errores técnicos de MongoDB, conversiones
 * inválidas o problemas al acceder a la base de datos.
 */
public class NegocioException extends Exception {

    public NegocioException(String mensaje) {
        super(mensaje);
    }

    public NegocioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
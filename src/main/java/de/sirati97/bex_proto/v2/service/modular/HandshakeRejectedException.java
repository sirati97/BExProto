package de.sirati97.bex_proto.v2.service.modular;

/**
 * Created by sirati97 on 15.04.2016.
 */
public class HandshakeRejectedException extends HandshakeException {

    public HandshakeRejectedException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * java.security.PrivilegedActionException}).
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link Throwable#getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since  1.5
     */
    public HandshakeRejectedException(Throwable cause) {
        super(cause);
    }

}

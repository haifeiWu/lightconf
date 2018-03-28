package com.lightconf.core.exception;

/**
 * lightconf exception
 *
 * @author wuhf
 */
public class LightConfException extends RuntimeException {

    private static final long serialVersionUID = 42L;

    public LightConfException(String msg) {
        super(msg);
    }

    public LightConfException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public LightConfException(Throwable cause) {
        super(cause);
    }

}

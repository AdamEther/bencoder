package com.github.adamether.bencoder.exception;

/**
 * @author adamether
 */

public class BencodeSerializationException extends RuntimeException {

    public BencodeSerializationException() {
    }

    public BencodeSerializationException(String message) {
        super(message);
    }

    public BencodeSerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BencodeSerializationException(Throwable cause) {
        super(cause);
    }

}

package com.github.adamether.bencoder.exception;

/**
 * @author adamether
 */
public class BencodeDeserializationException extends RuntimeException {

    public BencodeDeserializationException() {
    }

    public BencodeDeserializationException(String message) {
        super(message);
    }

    public BencodeDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BencodeDeserializationException(Throwable cause) {
        super(cause);
    }

}

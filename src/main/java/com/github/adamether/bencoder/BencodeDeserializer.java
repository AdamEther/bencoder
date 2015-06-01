package com.github.adamether.bencoder;

/**
 * @author adamether
 */
public interface BencodeDeserializer {

    <T> T deserialize(byte[] from, Class<T> typeOfResult);

}

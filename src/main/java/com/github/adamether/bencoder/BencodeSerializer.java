package com.github.adamether.bencoder;

/**
 * @author adamether
 */
public interface BencodeSerializer {

    byte[] serialize(Object from);

}

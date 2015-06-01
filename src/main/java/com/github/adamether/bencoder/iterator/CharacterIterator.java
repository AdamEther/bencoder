package com.github.adamether.bencoder.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author adamether
 */
public class CharacterIterator implements Iterator<Character> {

    private final byte[] bytes;

    private int position;

    public CharacterIterator(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public boolean hasNext() {
        return position < bytes.length;
    }

    @Override
    public Character next() {
        if (hasNext()) {
            return (char) bytes[position++];
        } else {
            throw new NoSuchElementException();
        }
    }

}

package com.github.adamether.bencoder.mapper;

import com.github.adamether.bencoder.exception.BencodeDeserializationException;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author adamether
 */
public class BaseMapper {

    protected BaseMapper() {}

    public static final Charset CHARSET = Charset.forName("utf-8");

    protected static final char LIST_START_WITH       = 'l';
    protected static final char DICTIONARY_START_WITH = 'd';
    protected static final char INT_START_WITH        = 'i';
    protected static final char MINUS_AFTER_START     = '-';
    protected static final char STRING_DELIMITER      = ':';
    protected static final char NOT_STRING_END_WITH   = 'e';

    protected static byte[] getBytes(Object anObject) {
        return String.valueOf(anObject).getBytes(CHARSET);
    }

    protected static char getNextCharacter(Iterator<Character> iterator) {
        try {
            return iterator.next();
        } catch (NoSuchElementException e) {
            throw new BencodeDeserializationException("Unexpected end of character sequence", e);
        }
    }

}

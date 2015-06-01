package com.github.adamether.bencoder.mapper;

import com.github.adamether.bencoder.exception.BencodeDeserializationException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import static java.lang.Character.getNumericValue;
import static java.lang.Character.isDigit;

/**
 * @author adamether
 */
public final class StringMapper extends BaseMapper {

    private StringMapper() {}

    public static byte[] serialize(String string) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(getBytes(string.length()));
        outputStream.write(getBytes(STRING_DELIMITER));
        outputStream.write(getBytes(string));
        return outputStream.toByteArray();
    }

    public static String deserialize(char charFrom, Iterator<Character> iterator) {
        int length = deserializeStringLength(charFrom, iterator);
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = getNextCharacter(iterator);
        }
        return new String(chars);
    }

    private static int deserializeStringLength(char from, Iterator<Character> iterator) {
        int numericValue = getNumericValue(from);
        char character;
        while (true) {
            character = getNextCharacter(iterator);
            if (isDigit(character)) {
                numericValue = 10 * numericValue + getNumericValue(character);
            } else if (character == STRING_DELIMITER) {
                break;
            } else {
                throw new BencodeDeserializationException(
                        "Unexpected character while parsing string length: [" + character + "]"
                );
            }
        }
        return numericValue;
    }

}

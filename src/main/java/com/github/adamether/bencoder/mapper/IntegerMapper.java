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
public final class IntegerMapper extends BaseMapper {

    private IntegerMapper() {}

    public static byte[] serialize(Integer integer) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(getBytes(INT_START_WITH));
        outputStream.write(getBytes(integer));
        outputStream.write(getBytes(NOT_STRING_END_WITH));
        return outputStream.toByteArray();
    }

    public static Integer deserialize(Iterator<Character> iterator) {
        int sign = 1;
        int numericValue = 0;
        char character;
        while (true) {
            character = getNextCharacter(iterator);
            if (numericValue == 0 && character == MINUS_AFTER_START) {
                sign = -1 * sign;
            } else if (isDigit(character)) {
                numericValue = 10 * numericValue + getNumericValue(character);
            } else if (character == NOT_STRING_END_WITH) {
                break;
            } else {
                throw new BencodeDeserializationException(
                        "Unexpected character while parsing numeric: [" + character + "]"
                );
            }
        }
        return sign * numericValue;
    }

}

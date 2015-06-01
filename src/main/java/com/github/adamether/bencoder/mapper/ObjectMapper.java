package com.github.adamether.bencoder.mapper;

import com.github.adamether.bencoder.exception.BencodeDeserializationException;
import com.github.adamether.bencoder.exception.BencodeSerializationException;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static java.lang.Character.isDigit;

/**
 * @author adamether
 */
public final class ObjectMapper extends BaseMapper {

    private ObjectMapper() {}

    public static byte[] serialize(Object object) throws IOException {
        if (object instanceof Integer) {
            return IntegerMapper.serialize(
                    (Integer) object
            );
        } else if (object instanceof String) {
            return StringMapper.serialize(
                    (String) object
            );
        } else if (object instanceof List<?>) {
            return ListMapper.serialize(
                    (List<?>) object
            );
        } else {
            throw new BencodeSerializationException(
                    "Unsupported object type : [" + object.getClass() + "]"
            );
        }
    }

    public static Object deserialize(Iterator<Character> iterator) {
        char character;
        if (iterator.hasNext()) {
            character = iterator.next();
            if (character == INT_START_WITH) {
                return IntegerMapper.deserialize(iterator);
            } else if (isDigit(character)) {
                return StringMapper.deserialize(character, iterator);
            } else if (character == LIST_START_WITH) {
                return ListMapper.deserialize(iterator);
            } else if (character == DICTIONARY_START_WITH) {
                return DictionaryMapper.deserialize(iterator);
            } else {
                throw new BencodeDeserializationException(
                        "Unexpected character while parsing object: [" + character + "]"
                );
            }
        } else {
            return null;
        }
    }

}

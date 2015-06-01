package com.github.adamether.bencoder.mapper;

import com.github.adamether.bencoder.exception.BencodeDeserializationException;
import com.github.adamether.bencoder.exception.BencodeSerializationException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import static java.lang.Character.isDigit;

/**
 * @author adamether
 */
public final class DictionaryMapper extends BaseMapper {

    private DictionaryMapper() {}

    public static byte[] serialize(Entry<?, ?> entry) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (entry.getKey() instanceof String) {
            outputStream.write(
                    ObjectMapper.serialize(entry.getKey())
            );
            outputStream.write(
                    ObjectMapper.serialize(entry.getValue())
            );
        } else {
            throw new BencodeSerializationException(
                    "The dictionary key must be string, but was: [" + entry.getKey() + "]"
            );
        }
        return outputStream.toByteArray();
    }

    public static List<Entry<?, ?>> deserialize(Iterator<Character> iterator) {
        List<Entry<?, ?>> list = new LinkedList<>();
        char character;
        while (true) {
            character = getNextCharacter(iterator);
            if (isDigit(character)) {
                list.add(
                        new SimpleEntry<>(
                                StringMapper.deserialize(character, iterator),
                                ObjectMapper.deserialize(iterator)
                        )
                );
            } else if (character == NOT_STRING_END_WITH) {
                break;
            } else {
                throw new BencodeDeserializationException(
                        "Unexpected character while parsing dictionary: [" + character + "]"
                );
            }
        }
        return list;
    }

}

package com.github.adamether.bencoder.mapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import static java.lang.Character.isDigit;

/**
 * @author adamether
 */
public final class ListMapper extends BaseMapper {

    private ListMapper() {}

    public static byte[] serialize(List<?> list) throws IOException {
        final Object firstObject = list.get(0);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (firstObject instanceof Entry) {
            @SuppressWarnings("unchecked")
            final List<Entry<?, ?>> entries = (List<Entry<?, ?>>) list;
            outputStream.write(DICTIONARY_START_WITH);
            for (Entry<?, ?> entry : entries) {
                outputStream.write(
                        DictionaryMapper.serialize(entry)
                );
            }
        } else {
            outputStream.write(LIST_START_WITH);
            for (Object object : list) {
                outputStream.write(
                        ObjectMapper.serialize(object)
                );
            }
        }
        outputStream.write(NOT_STRING_END_WITH);
        return outputStream.toByteArray();

    }

    public static List<?> deserialize(Iterator<Character> iterator) {
        List<Object> list = new LinkedList<>();
        char character;
        while (true) {
            character = getNextCharacter(iterator);
            if (character == INT_START_WITH) {
                list.add(
                        IntegerMapper.deserialize(iterator)
                );
            } else if (isDigit(character)) {
                list.add(
                        StringMapper.deserialize(character, iterator)
                );
            } else if (character == LIST_START_WITH) {
                list.add(
                        ListMapper.deserialize(iterator)
                );
            } else if (character == DICTIONARY_START_WITH) {
                list.add(
                        DictionaryMapper.deserialize(iterator)
                );
            } else if (character == NOT_STRING_END_WITH) {
                break;
            }
        }
        return list;
    }

}

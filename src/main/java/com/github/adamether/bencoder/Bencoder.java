package com.github.adamether.bencoder;

import com.github.adamether.bencoder.exception.BencodeSerializationException;
import com.github.adamether.bencoder.iterator.CharacterIterator;
import com.github.adamether.bencoder.mapper.ObjectMapper;

import java.io.IOException;

/**
 * <p>
 *     Bencoder is responsible for serialization and deserialization of "b-encode" data format.
 * </p>
 *
 * <p>
 *     Bencode (pronounced like B encode) is the encoding used by the peer-to-peer file sharing
 * system BitTorrent for storing and transmitting loosely structured data.
 * It supports four different types of values:
 *     <ul>
 *         <li>byte strings as String class;</li>
 *         <li>integers as Integer class;</li>
 *         <li>dictionaries (associative arrays) LinkedList<Entry<String, ?>> class;</li>
 *         <li>list as LinkedList (of any bencode object) class</li>
 *     </ul>
 * </p>
 *
 *
 * <p>
 *     According <a href="https://ru.wikipedia.org/wiki/Bencode">Bencode format description</a>
 * the encoded data representing a chain of bytes that does not imply any charset.
 * In this implementation meant that the source data are located in the <b>"utf-8"</b> charset.
 * </p>
 *
 * @author adamether
 */
public class Bencoder implements BencodeDeserializer, BencodeSerializer {

    /**
     * Encode the given object to byte array (in "utf-8" charset)
     *
     * @param from - an object one of these type: Integer, String, List, List<Entry<String, ?>>
     * @return - byte array witch imply "utf-8" charset
     */
    @Override
    public byte[] serialize(Object from) {
        try {
            return ObjectMapper.serialize(from);
        } catch (IOException e) {
            throw new BencodeSerializationException(e);
        }
    }

    /**
     * Decode the given object to byte array (in "utf-8" charset)
     *
     * @param from - byte array witch imply "utf-8" charset (must be not null !)
     * @param typeOfResult - class type one of these types:
     *                     Integer / String / List / List<Entry<String, ?>> (LinkedList under the hood)
     * @param <T> - generic param
     * @return return an object of typeOfResult (return null if "from" param is empty)
     */
    @Override
    public <T> T deserialize(byte[] from, Class<T> typeOfResult) {
        return typeOfResult.cast(
                ObjectMapper.deserialize(
                        new CharacterIterator(from)
                )
        );
    }

}

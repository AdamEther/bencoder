package com.github.adamether.bencoder;

import com.github.adamether.bencoder.exception.BencodeDeserializationException;
import com.github.adamether.bencoder.exception.BencodeSerializationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.github.adamether.bencoder.mapper.ObjectMapper.CHARSET;
import static org.junit.Assert.*;

/**
 * Unit test for Bencoder module
 *
 * @author adamether
 */
@RunWith(JUnit4.class)
public class BencoderTest {

    private Bencoder bencoder = new Bencoder();

    /**
     * --- Test data generators ---
     */

    private static List<?> getBencodableList() {
        final List<Object> list = new LinkedList<>();

        list.add("spam");
        list.add(42);

        final List<Object> dictionary = new LinkedList<>();
        dictionary.add(new SimpleEntry<>("bar", "spam"));
        dictionary.add(new SimpleEntry<>("foo", "42"));

        list.add(dictionary);

        return list;
    }

    private static List<?> getBencodableDictionary() {
        final List<Object> dictionary = new LinkedList<>();
        dictionary.add(new SimpleEntry<>("bar", "spam"));
        return dictionary;
    }

    private static List<?> getNonBencodableDictionary() {
        List<Object> list = new LinkedList<>();
        list.add(new SimpleEntry<>("This is the ordinal string key", "but the next is not"));
        list.add(new SimpleEntry<>(123, "pam pam pam"));
        return list;
    }

    private static List<?> getNonBencodableList() {
        List<Object> list = new LinkedList<>();
        list.add(new IOException());
        return list;
    }

    /**
     * --- Decoding tests ---
     */

    @Test
    @SuppressWarnings("unchecked")
    public void testDecodeSuccess() throws Exception {
        List<?> decoded = bencoder.deserialize("l4:spami42ed3:bar4:spam3:foo2:42ee".getBytes(CHARSET), List.class);
        assertNotNull(decoded);
        assertFalse(decoded.isEmpty());
        assertEquals("spam", decoded.get(0));
        assertEquals(42, decoded.get(1));
        List<Map.Entry<?, ?>> dict = (List<Map.Entry<?, ?>>) decoded.get(2);

        assertNotNull(dict);
        final Map.Entry<?, ?> firstEntry = dict.get(0);
        assertNotNull(firstEntry);
        assertEquals("bar", firstEntry.getKey());
        assertEquals("spam", firstEntry.getValue());

        final Map.Entry<?, ?> secondEntry = dict.get(1);
        assertNotNull(secondEntry);
        assertEquals("foo", secondEntry.getKey());
        assertEquals("42", secondEntry.getValue());
    }

    @Test
    public void testDecodeList() throws Exception {
        List<?> decoded = bencoder.deserialize("l4:spami42ee".getBytes(CHARSET), List.class);
        assertNotNull(decoded);
        assertFalse(decoded.isEmpty());
        assertEquals("spam", decoded.get(0));
        assertEquals(42, decoded.get(1));
    }

    @Test(expected = BencodeDeserializationException.class)
    public void testDecodeListFail() throws Exception {
        bencoder.deserialize("l3:qwe".getBytes(CHARSET), List.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDecodeDictionary() throws Exception {
        List<Map.Entry<String, String>> decoded = bencoder.deserialize("d3:bar4:spame".getBytes(CHARSET), List.class);
        assertNotNull(decoded);
        final Map.Entry<String, String> entry = decoded.get(0);
        assertNotNull(entry);
        assertEquals("bar", entry.getKey());
        assertEquals("spam", entry.getValue());
    }

    @Test(expected = BencodeDeserializationException.class)
    public void testDecodeDictionaryFail() throws Exception {
        bencoder.deserialize("d2:123:123i5ee".getBytes(CHARSET), List.class);
    }

    @Test
    public void testDecodeInt() throws Exception {
        final Integer decoded = bencoder.deserialize("i123e".getBytes(CHARSET), Integer.class);
        assertEquals(123, decoded.intValue());
    }

    @Test
    public void testDecodeIntNegative() throws Exception {
        final Integer decode = bencoder.deserialize("i-321e".getBytes(CHARSET), Integer.class);
        assertEquals(-321, decode.intValue());
    }

    @Test
    public void testDecodeIntZero() throws Exception {
        final Integer integer = bencoder.deserialize("i0e".getBytes(CHARSET), Integer.class);
        assertEquals(0, integer.intValue());
    }

    @Test(expected = BencodeDeserializationException.class)
    public void testDecodeIntFail() throws Exception {
        bencoder.deserialize("i1O1e".getBytes(CHARSET), Integer.class);
    }

    @Test(expected = BencodeDeserializationException.class)
    public void testDecodeIntUnexpectedCharacter() throws Exception {
        bencoder.deserialize("ia123e".getBytes(CHARSET), Integer.class);
    }

    @Test(expected = BencodeDeserializationException.class)
    public void testDecodeUnexpectedEnd() throws Exception {
        bencoder.deserialize("i12345".getBytes(CHARSET), Integer.class);
    }

    @Test
    public void testDecodeSimpleString() throws Exception {
        final String decoded = bencoder.deserialize("18:stringIsNotASpring".getBytes(CHARSET), String.class);
        assertEquals("stringIsNotASpring", decoded);
    }

    @Test
    public void testDecodeCuteString() throws Exception {
        final String decoded = bencoder.deserialize("13:^#@(=+-=)$%^!".getBytes(CHARSET), String.class);
        assertEquals("^#@(=+-=)$%^!", decoded);
    }

    @Test(expected = BencodeDeserializationException.class)
    public void testDecodeStringFail() throws Exception {
        bencoder.deserialize("7:qwerty".getBytes(CHARSET), String.class);
    }

    /**
     * --- Encoding tests ---
     */

    @Test
    public void testEncodeSuccess() throws Exception {
        final byte[] encoded = bencoder.serialize(getBencodableList());
        assertEquals("l4:spami42ed3:bar4:spam3:foo2:42ee", new String(encoded, CHARSET));
    }

    @Test
    public void testEncodeInt() throws Exception {
        final byte[] encoded = bencoder.serialize(123);
        assertEquals("i123e", new String(encoded, CHARSET));
    }

    @Test
    public void testEncodeIntNegative() throws Exception {
        final byte[] encoded = bencoder.serialize(-321);
        assertEquals("i-321e", new String(encoded, CHARSET));
    }

    @Test
    public void testEncodeIntZero() throws Exception {
        final byte[] encoded = bencoder.serialize(0);
        assertEquals("i0e", new String(encoded, CHARSET));
    }

    @Test
    public void testEncodeSimpleString() throws Exception {
        final byte[] encoded = bencoder.serialize("stringIsNotASpring");
        assertEquals("18:stringIsNotASpring", new String(encoded, CHARSET));
    }

    @Test
    public void testEncodeCuteString() throws Exception {
        final byte[] encoded = bencoder.serialize("^#@(=+-=)$%^!");
        assertEquals("13:^#@(=+-=)$%^!", new String(encoded, CHARSET));
    }

    @Test
    public void testEncodeList() throws Exception {
        final byte[] encoded = bencoder.serialize(getBencodableList());
        assertEquals("l4:spami42ed3:bar4:spam3:foo2:42ee", new String(encoded, CHARSET));
    }

    @Test
    public void testEncodeDictionary() throws Exception {
        final byte[] encoded = bencoder.serialize(getBencodableDictionary());
        assertEquals("d3:bar4:spame", new String(encoded, CHARSET));
    }

    @Test(expected = BencodeSerializationException.class)
    public void testEncodeDictionaryInvalidKey() throws Exception {
        bencoder.serialize(
                getNonBencodableDictionary()
        );
    }

    @Test(expected = BencodeSerializationException.class)
    public void testEncodeListFailUnsupportedType() throws Exception {
        bencoder.serialize(
                getNonBencodableList()
        );
    }

}

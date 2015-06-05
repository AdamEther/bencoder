# Bencoder


Bencoder is responsible for serialization and deserialization of "b-encode" data format.

Bencode (pronounced like B encode) is the encoding used by the peer-to-peer file sharing
system BitTorrent for storing and transmitting loosely structured data.


This library supports four different types of values:
* byte strings as `String` class;
* integers as `Integer` class;
* dictionaries (associative arrays) `List<Entry<String, ?>>` class;
* list as `List` (of any bencode object) class.

According [Bencode format description](ru.wikipedia.org/wiki/Bencode).
the encoded data representing a chain of bytes that does not imply any charset.
In this implementation meant that the source data are located in the *utf-8* charset.
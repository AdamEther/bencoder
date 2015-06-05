# Bencoder

<p>
  Bencoder is responsible for serialization and deserialization of "b-encode" data format.
</p>

<p>
  Bencode (pronounced like B encode) is the encoding used by the peer-to-peer file sharing
system BitTorrent for storing and transmitting loosely structured data.
</p>
<p>
This library supports four different types of values:
  <ul>
      <li>byte strings as `String` class;</li>
      <li>integers as `Integer` class;</li>
      <li>dictionaries (associative arrays) `List<Entry<String, ?>>` class;</li>
      <li>list as `List` (of any bencode object) class</li>
  </ul>
</p>

<p>
  According <a href="https://ru.wikipedia.org/wiki/Bencode">Bencode format description</a>
the encoded data representing a chain of bytes that does not imply any charset.
In this implementation meant that the source data are located in the <b>"utf-8"</b> charset.
</p>
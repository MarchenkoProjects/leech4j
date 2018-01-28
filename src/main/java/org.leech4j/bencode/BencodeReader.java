package org.leech4j.bencode;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Oleg Marchenko
 */

public final class BencodeReader {
    static final char NUMBER_PREFIX = 'i';
    static final char LIST_PREFIX = 'l';
    static final char MAP_PREFIX = 'd';
    static final char END_SUFFIX = 'e';
    static final char BYTE_ARRAY_LENGTH_DELIMITER = ':';

    public static Object read(String filename) {
        return read(new File(filename));
    }

    public static Object read(File file) {
        try {
            return read(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Object read(byte[] data) {
        return read(new ByteArrayInputStream(data));
    }

    public static Object read(InputStream is) {
        try(PushbackInputStream pis = new PushbackInputStream(is)) {
            return readNext(pis);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Object readNext(PushbackInputStream pis) throws IOException {
        int nextByte = pis.read();
        if (nextByte == NUMBER_PREFIX) {
            return readNumber(pis);
        } else if (Character.isDigit(nextByte)) {
            pis.unread(nextByte);
            return readByteArray(pis);
        } else if (nextByte == LIST_PREFIX) {
            return readList(pis);
        } else if (nextByte == MAP_PREFIX) {
            return readMap(pis);
        } else if (nextByte == END_SUFFIX) {
            return null;
        }
        throw new IllegalStateException("Invalid bencode");
    }

    private static Number readNumber(PushbackInputStream pis) throws IOException {
        return readNumber(pis, END_SUFFIX);
    }

    private static Number readNumber(PushbackInputStream pis, char endChar) throws IOException {
        StringBuilder numberBuilder = new StringBuilder(11);
        int nextByte;
        while ((nextByte = pis.read()) != endChar) {
            numberBuilder.append((char) nextByte);
        }
        return Long.valueOf(numberBuilder.toString());
    }

    private static byte[] readByteArray(PushbackInputStream pis) throws IOException {
        int length = readNumber(pis, BYTE_ARRAY_LENGTH_DELIMITER).intValue();

        byte[] byteArray = new byte[length];
        int offset = 0;
        while (length > 0) {
            int read = pis.read(byteArray, offset, length);
            length -= read;
            offset += read;
        }
        return byteArray;
    }

    private static List<Object> readList(PushbackInputStream pis) throws IOException {
        List<Object> list = new LinkedList<>();
        Object item;
        while ((item = readNext(pis)) != null) {
            list.add(item);
        }
        return list;
    }

    private static Map<String, Object> readMap(PushbackInputStream pis) throws IOException {
        Map<String, Object> map = new TreeMap<>();
        byte[] key;
        Object value;
        while ((key = (byte[]) readNext(pis)) != null && (value = readNext(pis)) != null) {
            map.put(new String(key), value);
        }
        return map;
    }

    private BencodeReader() {
    }
}

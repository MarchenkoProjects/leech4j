package org.leech4j.bencode;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

/**
 * @author Oleg Marchenko
 */

public final class BencodeWriter {
    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    public static void write(String filename, Object object) {
        write(new File(filename), object);
    }

    public static void write(File file, Object object) {
        try {
            write(new FileOutputStream(file), object);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public static byte[] write(Object object) {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream(64)) {
            write(baos, object);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void write(OutputStream os, Object object) {
        try {
            writeNext(os, object);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static void writeNext(OutputStream os, Object object) throws IOException {
        if (object instanceof Number) {
            writeNumber(os, (Number) object);
        } else if (object instanceof byte[]) {
            writeByteArray(os, (byte[]) object);
        } else if (object instanceof String) {
            String value = (String) object;
            writeByteArray(os, value.getBytes());
        } else if (object instanceof Collection) {
            writeCollection(os, (Collection<Object>) object);
        } else if (object instanceof Map) {
            writeMap(os, (Map<String, Object>) object);
        } else {
            throw new IllegalStateException("Unsupported object type");
        }
    }

    private static void writeNumber(OutputStream os, Number value) throws IOException {
        os.write(BencodeReader.NUMBER_PREFIX);
        os.write(UTF8_CHARSET.encode(value.toString()).array());
        os.write(BencodeReader.END_SUFFIX);
    }

    private static void writeByteArray(OutputStream os, byte[] value) throws IOException {
        os.write(UTF8_CHARSET.encode(String.valueOf(value.length)).array());
        os.write(BencodeReader.BYTE_ARRAY_LENGTH_DELIMITER);
        os.write(value);
    }

    private static void writeCollection(OutputStream os, Collection<Object> collection) throws IOException {
        os.write(BencodeReader.LIST_PREFIX);
        for (Object value: collection) {
            writeNext(os, value);
        }
        os.write(BencodeReader.END_SUFFIX);
    }

    private static void writeMap(OutputStream os, Map<String, Object> map) throws IOException {
        os.write(BencodeReader.MAP_PREFIX);
        for (Map.Entry<String, Object> entry: map.entrySet()) {
            writeNext(os, entry.getKey());
            writeNext(os, entry.getValue());
        }
        os.write(BencodeReader.END_SUFFIX);
    }

    private BencodeWriter() {
    }
}

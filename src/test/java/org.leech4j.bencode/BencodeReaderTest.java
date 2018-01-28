package org.leech4j.bencode;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Oleg Marchenko
 */

public class BencodeReaderTest {

    @Test
    public void readBencodeNumber() {
        Object readData = BencodeReader.read("i100e".getBytes());
        Assert.assertFalse(readData == null);
        Assert.assertTrue(readData instanceof Number);
        Assert.assertTrue(readData.equals(100L));
    }

    @Test
    public void readBencodeNegativeNumber() {
        Object readData = BencodeReader.read("i-100e".getBytes());
        Assert.assertFalse(readData == null);
        Assert.assertTrue(readData instanceof Number);
        Assert.assertTrue(readData.equals(-100L));
    }

    @Test
    public void readBencodeByteArray() {
        Object readData = BencodeReader.read("11:Test string".getBytes());
        Assert.assertFalse(readData == null);
        Assert.assertTrue(readData instanceof byte[]);
        Assert.assertTrue(Arrays.equals((byte[]) readData, "Test string".getBytes()));
    }

    @Test
    public void readBencodeList() {
        Object readData = BencodeReader.read("li100ei-100e11:Test stringe".getBytes());
        Assert.assertFalse(readData == null);
        Assert.assertTrue(readData instanceof List);

        List<Object> list = (List<Object>) readData;
        Assert.assertTrue(list.size() == 3);

        Assert.assertTrue(Long.valueOf(100).equals(list.get(0)));
        Assert.assertTrue(Long.valueOf(-100).equals(list.get(1)));
        Assert.assertTrue(Arrays.equals("Test string".getBytes(), (byte[]) list.get(2)));
    }

    @Test
    public void readBencodeMap() {
        Object readData = BencodeReader.read("d6:numberi100e10:byte array11:Test string4:listli100ei-100e11:Test stringee".getBytes());
        Assert.assertFalse(readData == null);
        Assert.assertTrue(readData instanceof Map);

        Map<String, Object> map = (Map<String, Object>) readData;
        Assert.assertTrue(map.size() == 3);

        Assert.assertTrue(map.containsKey("number"));
        Assert.assertTrue(map.containsKey("byte array"));
        Assert.assertTrue(map.containsKey("list"));

        Assert.assertTrue(Long.valueOf(100L).equals(map.get("number")));
        Assert.assertTrue(Arrays.equals("Test string".getBytes(), (byte[]) map.get("byte array")));

        List<Object> list = (List<Object>) map.get("list");
        Assert.assertTrue(list.size() == 3);

        Assert.assertTrue(Long.valueOf(100).equals(list.get(0)));
        Assert.assertTrue(Long.valueOf(-100).equals(list.get(1)));
        Assert.assertTrue(Arrays.equals("Test string".getBytes(), (byte[]) list.get(2)));
    }
}

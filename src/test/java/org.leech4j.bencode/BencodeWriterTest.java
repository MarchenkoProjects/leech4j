package org.leech4j.bencode;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Oleg Marchenko
 */

public class BencodeWriterTest {

    @Test
    public void writeNumber() {
        byte[] result = BencodeWriter.write(100);
        Assert.assertTrue(Arrays.equals(result, "i100e".getBytes()));
    }

    @Test
    public void writeNegativeNumber() {
        byte[] result = BencodeWriter.write(-100);
        Assert.assertTrue(Arrays.equals(result, "i-100e".getBytes()));
    }

    @Test
    public void writeEmptyByteArray() {
        byte[] result = BencodeWriter.write(new byte[0]);
        Assert.assertTrue(Arrays.equals(result, "0:".getBytes()));
    }

    @Test
    public void writeByteArray() {
        byte[] result = BencodeWriter.write("Test byte array".getBytes());
        Assert.assertTrue(Arrays.equals(result, "15:Test byte array".getBytes()));
    }

    @Test
    public void writeList() {
        List<? extends Object> list = Arrays.asList(100, -100, "Test byte array".getBytes());
        byte[] result = BencodeWriter.write(list);
        Assert.assertTrue(Arrays.equals(result, "li100ei-100e15:Test byte arraye".getBytes()));
    }

    @Test
    public void writeMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("number", 100);
        map.put("byte array", "Test byte array".getBytes());
        List<? extends Object> list = Arrays.asList(100, -100, "Test byte array".getBytes());
        map.put("list", list);

        byte[] result = BencodeWriter.write(map);
        Assert.assertTrue(Arrays.equals(result, "d6:numberi100e10:byte array15:Test byte array4:listli100ei-100e15:Test byte arrayee".getBytes()));
    }
}

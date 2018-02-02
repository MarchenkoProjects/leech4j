package org.leech4j.torrent;

/**
 * @author Oleg Marchenko
 */

public class SharedFile {

    private final String filename;
    private final long size;

    public SharedFile(String filename, long size) {
        this.filename = filename;
        this.size = size;
    }

    public String getFilename() {
        return filename;
    }

    public long getSize() {
        return size;
    }

    @Override
    public String toString() {
        return filename + " [" + size + ']';
    }
}

package org.leech4j.util;

import org.leech4j.bencode.BencodeReader;
import org.leech4j.bencode.BencodeWriter;
import org.leech4j.torrent.TorrentFile;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @author Oleg Marchenko
 */

public final class TorrentFileLoader {

    public static TorrentFile load(String filename) {
        return load(new File(filename));
    }

    public static TorrentFile load(File file) {
        try {
            return load(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static TorrentFile load(byte[] data) {
        return load(new ByteArrayInputStream(data));
    }

    @SuppressWarnings("unchecked")
    public static TorrentFile load(InputStream is) {
        Object readData = BencodeReader.read(is);
        if (!(readData instanceof Map)) {
            throw new IllegalStateException("Invalid torrent file structure");
        }

        TorrentFile.Builder torrentBuilder = new TorrentFile.Builder();

        Map<String, Object> torrentMetadata = (Map<String, Object>) readData;
        if (torrentMetadata.containsKey("announce")) {
            torrentBuilder.addTrackerUrl((String) torrentMetadata.get("announce"));
        }

        if (torrentMetadata.containsKey("announce-list")) {
            List<List<byte[]>> announceListList = (List<List<byte[]>>) torrentMetadata.get("announce-list");
            for (List<byte[]> announceList: announceListList) {
                torrentBuilder.addTrackerUrl(new String(announceList.get(0)));
            }
            torrentBuilder.sortTrackers();
        }

        if (torrentMetadata.containsKey("info")) {
            Map<String, Object> info = (Map<String, Object>) torrentMetadata.get("info");
            if (info.containsKey("files")) {
                // Multiple file mode.
                List<Map<String, Object>> files = (List<Map<String, Object>>) info.get("files");
                String rootFilePath = new String((byte[]) info.get("name"));
                long totalSize = 0;
                for (Map<String, Object> file: files) {
                    StringBuilder filePathBuilder = new StringBuilder();
                    filePathBuilder.append(rootFilePath).append(File.separator);

                    List<byte[]> path = (List<byte[]>) file.get("path");
                    for (byte[] partOfPath: path) {
                        filePathBuilder.append(new String(partOfPath)).append(File.separatorChar);
                    }
                    filePathBuilder.deleteCharAt(filePathBuilder.length() - 1);

                    Long fileSize = (Long) file.get("length");
                    torrentBuilder.addFile(filePathBuilder.toString(), fileSize);

                    totalSize += fileSize;
                }
                torrentBuilder.setTotalSize(totalSize);
            } else {
                // Single file mode.
                String filename = (String) info.get("name");
                Long fileSize = (Long) info.get("length");
                torrentBuilder.addFile(filename, fileSize);
                torrentBuilder.setTotalSize(fileSize);
            }

            if (info.containsKey("pieces")) {
                torrentBuilder.setPieces((byte[]) info.get("pieces"));
            }
            if (info.containsKey("piece length")) {
                torrentBuilder.setPieceSize(((Number) info.get("piece length")).intValue());
            }

            byte[] infoData = BencodeWriter.write(info);
            torrentBuilder.setInfoHash(HashType.SHA1.valueOf(infoData));
        }
        return torrentBuilder.build();
    }

    private TorrentFileLoader() {
    }
}

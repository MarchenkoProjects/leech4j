package org.leech4j.torrent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Oleg Marchenko
 */

public class TorrentFile {
    public static final String BYTE_ENCODING = "ISO-8859-1";

    private final List<TrackerUrl> trackers;
    private final List<SharedFile> files;
    private final byte[] pieces;
    private final int pieceSize;
    private final byte[] infoHash;
    private final long totalSize;

    private TorrentFile(List<TrackerUrl> trackers, List<SharedFile> files,
                        byte[] pieces, int pieceSize, byte[] infoHash, long totalSize) {
        this.trackers = trackers;
        this.files = files;
        this.pieces = pieces;
        this.pieceSize = pieceSize;
        this.infoHash = infoHash;
        this.totalSize = totalSize;
    }

    public List<TrackerUrl> getTrackers() {
        return trackers;
    }

    public List<SharedFile> getFiles() {
        return files;
    }

    public byte[] getPieces() {
        return pieces;
    }

    public int getPieceSize() {
        return pieceSize;
    }

    public byte[] getInfoHash() {
        return infoHash;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public static class Builder {

        private final List<TrackerUrl> trackers = new ArrayList<>();
        private final List<SharedFile> files = new ArrayList<>();
        private byte[] pieces;
        private int pieceSize;
        private byte[] infoHash;
        private long totalSize;

        public Builder addTrackerUrl(String announceUrl) {
            trackers.add(new TrackerUrl(announceUrl));
            return this;
        }

        public Builder sortTrackers() {
            Collections.sort(trackers);
            return this;
        }

        public Builder addFile(String filename, long fileSize) {
            files.add(new SharedFile(filename, fileSize));
            return this;
        }

        public Builder setPieces(byte[] pieces) {
            this.pieces = pieces;
            return this;
        }

        public Builder setPieceSize(int pieceSize) {
            this.pieceSize = pieceSize;
            return this;
        }

        public Builder setInfoHash(byte[] infoHash) {
            this.infoHash = infoHash;
            return this;
        }

        public Builder setTotalSize(long totalSize) {
            this.totalSize = totalSize;
            return this;
        }

        public TorrentFile build() {
            return new TorrentFile(trackers, files, pieces, pieceSize, infoHash, totalSize);
        }
    }
}

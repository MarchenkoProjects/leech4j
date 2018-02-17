package org.leech4j.announce;

import static org.leech4j.announce.AnnounceRequest.Event.STARTED;

/**
 * @author Oleg Marchenko
 */

public class AnnounceRequest {
    public static final int DEFAULT_NUM_WANT = 50;

    protected byte[] infoHash;
    protected byte[] clientId;
    protected int port;
    protected long downloaded;
    protected long uploaded;
    protected long left;
    protected Event event = STARTED;
    protected int numWant = DEFAULT_NUM_WANT;

    private AnnounceRequest() {
    }

    protected AnnounceRequest(AnnounceRequest request) {
        this.infoHash = request.infoHash;
        this.clientId = request.clientId;
        this.port = request.port;
        this.downloaded = request.downloaded;
        this.uploaded = request.uploaded;
        this.left = request.left;
        this.event = request.event;
        this.numWant = request.numWant;
    }

    public byte[] getInfoHash() {
        return infoHash;
    }

    public byte[] getClientId() {
        return clientId;
    }

    public int getPort() {
        return port;
    }

    public long getDownloaded() {
        return downloaded;
    }

    public long getUploaded() {
        return uploaded;
    }

    public long getLeft() {
        return left;
    }

    public Event getEvent() {
        return event;
    }

    public int getNumWant() {
        return numWant;
    }

    public enum Event {
        NONE(0),
        COMPLETED(1),
        STARTED(2),
        STOPPED(3);

        private final int id;

        Event(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    public static Builder newBuilder() {
        return new AnnounceRequest().new Builder();
    }

    public class Builder {

        public Builder setInfoHash(byte[] infoHash) {
            AnnounceRequest.this.infoHash = infoHash;
            return this;
        }

        public Builder setClientId(byte[] clientId) {
            AnnounceRequest.this.clientId = clientId;
            return this;
        }

        public Builder setPort(int port) {
            AnnounceRequest.this.port = port;
            return this;
        }

        public Builder setDownloaded(long downloaded) {
            AnnounceRequest.this.downloaded = downloaded;
            return this;
        }

        public Builder setUploaded(long uploaded) {
            AnnounceRequest.this.uploaded = uploaded;
            return this;
        }

        public Builder setLeft(long left) {
            AnnounceRequest.this.left = left;
            return this;
        }

        public Builder setNumWant(int numWant) {
            AnnounceRequest.this.numWant = numWant;
            return this;
        }

        public Builder setEvent(Event event) {
            AnnounceRequest.this.event = event;
            return this;
        }

        public AnnounceRequest build() {
            return AnnounceRequest.this;
        }
    }
}

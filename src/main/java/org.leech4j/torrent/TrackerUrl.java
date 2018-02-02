package org.leech4j.torrent;

import static java.lang.Integer.compare;
import static org.leech4j.torrent.TrackerUrl.ProtocolType.getProtocolByUrl;

/**
 * @author Oleg Marchenko
 */

public class TrackerUrl implements Comparable<TrackerUrl> {

    private final String url;
    private final ProtocolType protocol;

    public TrackerUrl(String url) {
        this.url = url;
        this.protocol = getProtocolByUrl(url);
    }

    public String getUrl() {
        return url;
    }

    public ProtocolType getProtocol() {
        return protocol;
    }

    @Override
    public int compareTo(TrackerUrl trackerUrl) {
        return compare(trackerUrl.protocol.priority(), protocol.priority());
    }

    @Override
    public String toString() {
        return url + " [" + protocol + ']';
    }

    public enum ProtocolType {

        UDP(2),
        HTTP(1),
        UNKNOWN(0);

        private final int priority;

        ProtocolType(int priority) {
            this.priority = priority;
        }

        public int priority() {
            return priority;
        }

        public static ProtocolType getProtocolByUrl(String url) {
            if (url.startsWith("http")) {
                return HTTP;
            }
            if (url.startsWith("udp")) {
                return UDP;
            }
            return UNKNOWN;
        }
    }
}

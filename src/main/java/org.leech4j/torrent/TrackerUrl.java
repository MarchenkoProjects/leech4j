package org.leech4j.torrent;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.Integer.compare;
import static org.leech4j.torrent.TrackerUrl.ProtocolType.valueOf;

/**
 * @author Oleg Marchenko
 */

public class TrackerUrl implements Comparable<TrackerUrl> {

    private final URI url;
    private final ProtocolType protocol;

    public TrackerUrl(String url) {
        try {
            this.url = new URI(url);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
        this.protocol = valueOf(this.url);
    }

    public String getUrl() {
        return url.toString();
    }

    public InetSocketAddress getAddress() {
        return new InetSocketAddress(url.getHost(), url.getPort());
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

        public static ProtocolType valueOf(URI url) {
            switch (url.getScheme()) {
                case "http":
                    return HTTP;
                case "udp":
                    return UDP;
                default:
                    return UNKNOWN;
            }
        }
    }
}

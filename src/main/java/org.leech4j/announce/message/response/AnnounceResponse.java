package org.leech4j.announce.message.response;

import org.leech4j.bencode.BencodeReader;
import org.leech4j.peer.Peer;
import org.leech4j.util.IpAddressUtils;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Oleg Marchenko
 */

public class AnnounceResponse {

    private final int interval;
    private final int incomplete;
    private final int complete;
    private final List<Peer> peers;

    private AnnounceResponse(int interval, int incomplete, int complete, List<Peer> peers) {
        this.interval = interval;
        this.incomplete = incomplete;
        this.complete = complete;
        this.peers = peers;
    }

    public int getInterval() {
        return interval;
    }

    public int getIncomplete() {
        return incomplete;
    }

    public int getComplete() {
        return complete;
    }

    public List<Peer> getPeers() {
        return peers;
    }

    @SuppressWarnings("unchecked")
    public static AnnounceResponse parse(InputStream is) {
        Object bencodeResponse = BencodeReader.read(is);
        if (!(bencodeResponse instanceof Map)) {
            throw new IllegalStateException("Invalid tracker response");
        }

        Map<String, Object> trackerResponse = (Map<String, Object>) bencodeResponse;
        if (trackerResponse.containsKey("failure reason")) {
            throw new IllegalStateException("Internal tracker error " + trackerResponse.get("failure reason"));
        }

        int interval = ((Long) trackerResponse.get("interval")).intValue();
        int incomplete = ((Long) trackerResponse.get("incomplete")).intValue();
        int complete = ((Long) trackerResponse.get("complete")).intValue();

        Object peerList = trackerResponse.get("peers");
        if (peerList instanceof byte[]) {
            ByteBuffer peerListBuffer = ByteBuffer.wrap((byte[]) peerList);

            List<Peer> peers = new ArrayList<>(peerListBuffer.capacity() / 6);
            while(peerListBuffer.remaining() > 5) {
                String ip = IpAddressUtils.toIp(peerListBuffer.getInt());
                int port = IpAddressUtils.alignPort(peerListBuffer.getShort());
                peers.add(new Peer(ip, port));
            }
            return new AnnounceResponse(interval, incomplete, complete, peers);
        }
        if (peerList instanceof Map) {
            throw new UnsupportedOperationException("Response contains a list of users in a not compact form");
        }
        throw new IllegalStateException("Cannot parse tracker response");
    }
}

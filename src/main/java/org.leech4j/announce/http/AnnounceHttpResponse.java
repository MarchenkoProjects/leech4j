package org.leech4j.announce.http;

import org.leech4j.announce.AnnounceResponse;
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

class AnnounceHttpResponse extends AnnounceResponse {

    private AnnounceHttpResponse(int interval, int incomplete, int complete, List<Peer> peers) {
        super(interval, incomplete, complete, peers);
    }

    @SuppressWarnings("unchecked")
    public static AnnounceHttpResponse parse(InputStream is) {
        Object bencodeResponse = BencodeReader.read(is);
        if (!(bencodeResponse instanceof Map)) {
            throw new IllegalStateException("Invalid HTTP tracker response");
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
            return new AnnounceHttpResponse(interval, incomplete, complete, peers);
        }
        if (peerList instanceof Map) {
            throw new UnsupportedOperationException("Response contains a list of users in a not compact form");
        }
        throw new IllegalStateException("Cannot parse HTTP tracker response");
    }
}

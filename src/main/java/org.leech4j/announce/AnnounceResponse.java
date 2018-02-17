package org.leech4j.announce;

import org.leech4j.peer.Peer;

import java.util.List;

/**
 * @author Oleg Marchenko
 */

public class AnnounceResponse {

    private final int interval;
    private final int incomplete;
    private final int complete;
    private final List<Peer> peers;

    protected AnnounceResponse(int interval, int incomplete, int complete, List<Peer> peers) {
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
}

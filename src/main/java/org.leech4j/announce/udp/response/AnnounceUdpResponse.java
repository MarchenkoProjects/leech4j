package org.leech4j.announce.udp.response;

import org.leech4j.announce.AnnounceResponse;
import org.leech4j.announce.udp.UdpActionType;
import org.leech4j.peer.Peer;
import org.leech4j.util.IpAddressUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.leech4j.announce.AnnounceRequest.DEFAULT_NUM_WANT;
import static org.leech4j.announce.udp.UdpActionType.ANNOUNCE;

/**
 * @author Oleg Marchenko
 */

public class AnnounceUdpResponse extends AnnounceResponse implements UdpResponse {

    private final UdpActionType action;
    private final int transactionId;

    private AnnounceUdpResponse(UdpActionType action, int transactionId, int interval, int incomplete, int complete,
                                List<Peer> peers) {
        super(interval, incomplete, complete, peers);
        this.action = action;
        this.transactionId = transactionId;
    }

    @Override
    public boolean isValid(int transactionId) {
        return ANNOUNCE.equals(action) && this.transactionId == transactionId;
    }

    public UdpActionType getAction() {
        return action;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public static AnnounceUdpResponse parse(ByteBuffer data) {
        int action = data.getInt();
        int transactionId = data.getInt();
        int interval = data.getInt();
        int incomplete = data.getInt();
        int complete = data.getInt();

        List<Peer> peers = new ArrayList<>(DEFAULT_NUM_WANT);
        while(data.remaining() > 5) {
            String ip = IpAddressUtils.toIp(data.getInt());
            int port = IpAddressUtils.alignPort(data.getShort());
            peers.add(new Peer(ip, port));
        }
        return new AnnounceUdpResponse(UdpActionType.valueOf(action), transactionId, interval, incomplete, complete, peers);
    }
}

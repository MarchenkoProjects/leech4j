package org.leech4j.announce.udp.response;

import org.leech4j.announce.udp.UdpActionType;

import java.nio.ByteBuffer;

import static org.leech4j.announce.udp.UdpActionType.CONNECT;

/**
 * @author Oleg Marchenko
 */

public class ConnectUdpResponse implements UdpResponse {

    private final UdpActionType action;
    private final int transactionId;
    private final long connectionId;

    private ConnectUdpResponse(UdpActionType action, int transactionId, long connectionId) {
        this.action = action;
        this.transactionId = transactionId;
        this.connectionId = connectionId;
    }

    @Override
    public boolean isValid(int transactionId) {
        return CONNECT.equals(action) && this.transactionId == transactionId;
    }

    public UdpActionType getAction() {
        return action;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public long getConnectionId() {
        return connectionId;
    }

    public static ConnectUdpResponse parse(ByteBuffer data) {
        return new ConnectUdpResponse(UdpActionType.valueOf(data.getInt()), data.getInt(), data.getLong());
    }
}

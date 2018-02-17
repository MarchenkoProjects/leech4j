package org.leech4j.announce.udp.request;

import java.nio.ByteBuffer;

import static org.leech4j.announce.udp.UdpActionType.CONNECT;

/**
 * @author Oleg Marchenko
 */

public class ConnectUdpRequest implements UdpRequest {
    private static final int MESSAGE_DATA_SIZE = 16;
    private static final long PROTOCOL_ID = 0x41727101980L;

    private final int transactionId;

    private ConnectUdpRequest(int transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public byte[] getData() {
        ByteBuffer message = ByteBuffer.allocate(MESSAGE_DATA_SIZE);
        message.putLong(PROTOCOL_ID);
        message.putInt(CONNECT.getId());
        message.putInt(transactionId);
        return message.array();
    }

    public int getTransactionId() {
        return transactionId;
    }

    public static ConnectUdpRequest create(int transactionId) {
        return new ConnectUdpRequest(transactionId);
    }
}

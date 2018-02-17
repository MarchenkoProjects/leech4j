package org.leech4j.announce.udp.request;

import org.leech4j.announce.AnnounceRequest;

import java.nio.ByteBuffer;

import static org.leech4j.announce.udp.UdpActionType.ANNOUNCE;

/**
 * @author Oleg Marchenko
 */

public class AnnounceUdpRequest extends AnnounceRequest implements UdpRequest {
    private static final int MESSAGE_DATA_SIZE = 98;
    private static final int DEFAULT_KEY = 0;
    private static final int WITHOUT_IP_ADDRESS = 0;

    private long connectionId;
    private int transactionId;

    public AnnounceUdpRequest(AnnounceRequest request) {
        super(request);
    }

    public AnnounceUdpRequest(AnnounceRequest request, long connectionId, int transactionId) {
        super(request);
        this.connectionId = connectionId;
        this.transactionId = transactionId;
    }

    @Override
    public byte[] getData() {
        ByteBuffer message = ByteBuffer.allocate(MESSAGE_DATA_SIZE);
        message.putLong(connectionId);
        message.putInt(ANNOUNCE.getId());
        message.putInt(transactionId);
        message.put(infoHash);
        message.put(clientId);
        message.putLong(downloaded);
        message.putLong(left);
        message.putLong(uploaded);
        message.putInt(event.getId());
        message.putInt(WITHOUT_IP_ADDRESS);
        message.putInt(DEFAULT_KEY);
        message.putInt(numWant);
        message.putShort((short) port);
        return message.array();
    }
}

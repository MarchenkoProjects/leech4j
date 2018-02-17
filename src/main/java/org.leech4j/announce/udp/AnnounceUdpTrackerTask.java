package org.leech4j.announce.udp;

import org.leech4j.announce.AnnounceRequest;
import org.leech4j.announce.AnnounceResponse;
import org.leech4j.announce.AnnounceTrackerTask;
import org.leech4j.announce.udp.request.AnnounceUdpRequest;
import org.leech4j.announce.udp.request.ConnectUdpRequest;
import org.leech4j.announce.udp.response.AnnounceUdpResponse;
import org.leech4j.announce.udp.response.ConnectUdpResponse;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Random;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.leech4j.announce.udp.AnnounceUdpTrackerTask.ActionState.ANNOUNCE;
import static org.leech4j.announce.udp.AnnounceUdpTrackerTask.ActionState.CONNECT;

/**
 * @author Oleg Marchenko
 */

public final class AnnounceUdpTrackerTask extends AnnounceTrackerTask {
    private static final int MAX_ATTEMPTS = 8;
    private static final long CONNECTION_ID_EXPIRED_TIMEOUT = SECONDS.toMillis(60);

    private final InetSocketAddress address;
    private final AnnounceRequest request;

    public AnnounceUdpTrackerTask(InetSocketAddress address, AnnounceRequest request) {
        this.address = address;
        this.request = request;
    }

    @Override
    protected AnnounceResponse announceTracker() throws Exception {
        try(DatagramSocket socket = new DatagramSocket()) {
            socket.connect(address);

            byte[] requestData;
            ByteBuffer buffer;
            ConnectUdpResponse connectResponse = null;

            ActionState state = CONNECT;
            int attempts = 0;
            while (attempts < MAX_ATTEMPTS) {
                int transactionId = generateTransaction();
                switch (state) {
                    case CONNECT:
                        requestData = ConnectUdpRequest.create(transactionId).getData();
                        socket.send(new DatagramPacket(requestData, requestData.length));

                        buffer = ByteBuffer.allocate(16);
                        try {
                            socket.setSoTimeout((int) calculateTimeout(attempts));
                            socket.receive(new DatagramPacket(buffer.array(), buffer.capacity()));
                        } catch (SocketTimeoutException e) {
                            attempts++;
                            continue;
                        }

                        connectResponse = ConnectUdpResponse.parse(buffer);
                        if (!connectResponse.isValid(transactionId)) {
                            attempts++;
                            continue;
                        }

                        attempts = 0;
                        state = ANNOUNCE;
                        break;
                    case ANNOUNCE:
                        long timeout = calculateTimeout(attempts);
                        if (timeout >= CONNECTION_ID_EXPIRED_TIMEOUT) {
                            connectResponse = null;
                            attempts = 0;
                            state = CONNECT;
                            continue;
                        }

                        requestData = new AnnounceUdpRequest(request, connectResponse.getConnectionId(), transactionId).getData();
                        socket.send(new DatagramPacket(requestData, requestData.length));

                        DatagramPacket packet;
                        try {
                            socket.setSoTimeout((int) timeout);
                            packet = new DatagramPacket(new byte[1024], 1024);
                            socket.receive(packet);
                        } catch (SocketTimeoutException e) {
                            attempts++;
                            continue;
                        }

                        AnnounceUdpResponse announceResponse = AnnounceUdpResponse.parse(ByteBuffer.wrap(packet.getData(), 0, packet.getLength()));
                        if (!announceResponse.isValid(transactionId)) {
                            attempts++;
                            continue;
                        }
                        return announceResponse;
                }
            }
        }
        throw new IllegalStateException();
    }

    private int generateTransaction() {
        return new Random().nextInt();
    }

    private long calculateTimeout(int attempt) {
        return SECONDS.toMillis(15 * (int) Math.pow(2, attempt));
    }

    enum ActionState {
        CONNECT,
        ANNOUNCE
    }
}

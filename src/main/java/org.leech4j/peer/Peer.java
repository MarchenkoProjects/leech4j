package org.leech4j.peer;

/**
 * @author Oleg Marchenko
 */

public class Peer {

    private final String ip;
    private final int port;

    public Peer(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return ip + ':' + port;
    }
}

package org.leech4j.announce.udp;

/**
 * @author Oleg Marchenko
 */

public enum UdpActionType {
    CONNECT(0),
    ANNOUNCE(1),
    SCRAPE(2),
    ERROR(3);

    private final int id;

    UdpActionType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static UdpActionType valueOf(int id) {
        switch (id) {
            case 0:
                return CONNECT;
            case 1:
                return ANNOUNCE;
            case 2:
                return SCRAPE;
            default:
                return ERROR;
        }
    }
}

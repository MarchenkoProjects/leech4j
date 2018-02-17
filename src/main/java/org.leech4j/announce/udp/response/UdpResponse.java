package org.leech4j.announce.udp.response;

/**
 * @author Oleg Marchenko
 */

public interface UdpResponse {

    boolean isValid(int transactionId);
}

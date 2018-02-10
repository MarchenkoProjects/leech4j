package org.leech4j.util;

/**
 * @author Oleg Marchenko
 */

public final class IpAddressUtils {

    public static String toIp(int addr) {
        return ((addr >> 24) & 0xFF) + "." + ((addr >> 16) & 0xFF) + "." + ((addr >> 8) & 0xFF) + "." + (addr & 0xFF);
    }

    public static int alignPort(short port) {
        return port & 0xFFFF;
    }

    private IpAddressUtils() {
    }
}

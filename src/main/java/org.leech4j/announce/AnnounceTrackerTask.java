package org.leech4j.announce;

import org.leech4j.announce.http.AnnounceHttpTrackerTask;
import org.leech4j.announce.udp.AnnounceUdpTrackerTask;
import org.leech4j.torrent.TrackerUrl;

import java.util.concurrent.Callable;

/**
 * @author Oleg Marchenko
 */

public abstract class AnnounceTrackerTask implements Callable<AnnounceResponse> {

    protected AnnounceTrackerTask() {
    }

    @Override
    public AnnounceResponse call() throws Exception {
        return announceTracker();
    }

    protected abstract AnnounceResponse announceTracker() throws Exception;

    public static AnnounceTrackerTask create(TrackerUrl trackerUrl, AnnounceRequest request) {
        switch (trackerUrl.getProtocol()) {
            case HTTP:
                return new AnnounceHttpTrackerTask(trackerUrl.getUrl(), request);
            case UDP:
                return new AnnounceUdpTrackerTask(trackerUrl.getAddress(), request);
            default:
                throw new IllegalArgumentException("Unknown tracker protocol");
        }
    }
}

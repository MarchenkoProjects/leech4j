package org.leech4j.announce;

import org.leech4j.announce.message.request.AnnounceRequest;
import org.leech4j.announce.message.response.AnnounceResponse;
import org.leech4j.torrent.TrackerUrl;

import java.util.concurrent.Callable;

/**
 * @author Oleg Marchenko
 */

public abstract class AnnounceTrackerTask implements Callable<AnnounceResponse> {

    AnnounceTrackerTask() {
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
            default:
                throw new IllegalArgumentException("Unknown tracker protocol");
        }
    }
}

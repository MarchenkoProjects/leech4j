package org.leech4j.announce;

import org.leech4j.announce.message.request.AnnounceRequest;
import org.leech4j.announce.message.response.AnnounceHttpResponse;
import org.leech4j.announce.message.response.AnnounceResponse;
import org.leech4j.torrent.TorrentFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * @author Oleg Marchenko
 */

final class AnnounceHttpTrackerTask extends AnnounceTrackerTask {

    private final String trackerUrl;
    private final AnnounceRequest request;

    AnnounceHttpTrackerTask(String trackerUrl, AnnounceRequest request) {
        this.trackerUrl = trackerUrl;
        this.request = request;
    }

    @Override
    protected AnnounceResponse announceTracker() throws Exception {
        URLConnection connection = connectTo(buildAnnounceUrl());
        try (InputStream is = connection.getInputStream()) {
            return AnnounceHttpResponse.parse(is);
        }
    }

    private String buildAnnounceUrl() throws UnsupportedEncodingException {
        String infoHash = new String(request.getInfoHash(), TorrentFile.BYTE_ENCODING);
        String clientId = new String(request.getClientId(), TorrentFile.BYTE_ENCODING);
        return new StringBuilder(200)
                .append(trackerUrl)
                .append("?info_hash=").append(URLEncoder.encode(infoHash, TorrentFile.BYTE_ENCODING))
                .append("&peer_id=").append(URLEncoder.encode(clientId, TorrentFile.BYTE_ENCODING))
                .append("&port=").append(request.getPort())
                .append("&downloaded=").append(request.getDownloaded())
                .append("&uploaded=").append(request.getUploaded())
                .append("&left=").append(request.getLeft())
                .append("&numwant=").append(request.getNumWant())
                .append("&compact=").append(request.isCompact() ? 1 : 0)
                .append("&event=").append(request.getEvent().toString())
                .toString();
    }

    private URLConnection connectTo(String url) throws IOException {
        return new URL(url).openConnection();
    }
}

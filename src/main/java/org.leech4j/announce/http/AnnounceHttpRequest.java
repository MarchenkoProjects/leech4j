package org.leech4j.announce.http;

import org.leech4j.announce.AnnounceRequest;

/**
 * @author Oleg Marchenko
 */

class AnnounceHttpRequest extends AnnounceRequest {

    private boolean compact = true;

    AnnounceHttpRequest(AnnounceRequest request) {
        super(request);
    }

    AnnounceHttpRequest(AnnounceRequest request, boolean compact) {
        super(request);
        this.compact = compact;
    }

    public boolean isCompact() {
        return compact;
    }
}

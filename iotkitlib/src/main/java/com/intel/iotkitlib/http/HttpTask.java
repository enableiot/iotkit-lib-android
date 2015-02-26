package com.intel.iotkitlib.http;

/**
 * Created by dtran on 2/26/15.
 */
public interface HttpTask {
    CloudResponse doAsync(final String url, final HttpTaskHandler taskHandler);
    CloudResponse doSync(final String url);
}

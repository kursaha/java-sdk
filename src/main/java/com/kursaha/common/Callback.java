package com.kursaha.common;

/**
 * Call back for Async API operation
 */
public interface Callback {
    /**
     * Called in case of success
     */
    void onSuccess();

    /**
     * Called in case of failure
     */
    void onFailure();
}

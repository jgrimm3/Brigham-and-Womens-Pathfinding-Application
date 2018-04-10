package com.manlyminotaurs.pathfinding;

public class OrphanNodeException extends Exception {
    public OrphanNodeException() {
    }

    public OrphanNodeException(String message) {
        super(message);
    }

    public OrphanNodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrphanNodeException(Throwable cause) {
        super(cause);
    }

    public OrphanNodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

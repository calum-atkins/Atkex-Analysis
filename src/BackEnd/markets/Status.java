package BackEnd.markets;

/**
 * Current status of the market where the most recent price is at.
 * If a pattern is being formed it will be one of the selections within this class, if not, pending.
 */
public enum Status {
    ASCENDING_TRIANGLE,
    DESCENDING_TRIANGLE,
    PENNANT,
    DOUBLE_TOP,
    DOUBLE_BOTTOM,
    WEDGE,
    PENDING,
    SIGNAL;

    private Status() {
    }
}

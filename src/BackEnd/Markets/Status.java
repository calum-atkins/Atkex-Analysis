package BackEnd.Markets;

public enum Status {
    ASCENDING_TRIANGLE,
    DESCENDING_TRIANGLE,
    PENNANT,
    DOUBLE_TOP,
    DOUBLE_BOTTOM,
    WEDGE,
    PENDING;

    private Status() {
    }
}

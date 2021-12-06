package BackEnd.markets;

/**
 * Different time frames the application can handle.
 * Due the files list order within the RawMarketData folder, index'
 * are used to retrieve data for each timeframe,
 */
public enum MarketTimeframe {
    ONE_HOUR, //1 - array list index
    FOUR_HOUR, // 2 - array list index
    DAY; // 0 - array list index

    private MarketTimeframe() {
    }
}

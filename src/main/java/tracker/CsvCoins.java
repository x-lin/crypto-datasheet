package tracker;

import com.google.common.collect.ImmutableList;
import tracker.schema.coinmarketcap.CoinMarketCapTicker;

import java.util.Collection;
import java.util.List;

/**
 * @author LinX
 */
public class CsvCoins {
    public static List<String[]> toCsvList(final Collection<CsvCoin.Builder> coinBuilders) {
        final ImmutableList.Builder<String[]> builder = ImmutableList.builder();
        builder.add(CsvCoin.headerRow());
        coinBuilders.forEach(b -> builder.add(b.build().getRow()));
        return builder.build();
    }

    public static List<CsvCoin.Builder> builders(final Collection<CoinMarketCapTicker> coins) {
        return coins.stream().map(CsvCoin::builderFromCoin).collect(ImmutableList.toImmutableList());
    }
}

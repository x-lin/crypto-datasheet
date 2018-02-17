package tracker;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import me.joshmcfarlin.CryptoCompareAPI.Coins;
import me.joshmcfarlin.CryptoCompareAPI.Utils.OutOfCallsException;
import tracker.schema.coinmarketcap.CoinMarketCapTicker;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author LinX
 */
public class CsvCoins {
    private static final Gson GSON = new Gson();

    static List<String[]> fetchCoinDataAsCsv() {
        final List<CsvCoin.Builder> builders = extractDataFromApi();
        return toCsvList(builders);
    }

    private static List<CsvCoin.Builder> extractDataFromApi() {
        final HttpResponse<String> response = requestFromCoinMarketCap();
        final Set<CoinMarketCapTicker> coins = deserialize(response);

        final List<CsvCoin.Builder> builders = builders(coins);
        suplementWithCryptoCompareData(builders);
        return builders;
    }

    private static HttpResponse<String> requestFromCoinMarketCap() {
        try {
            return CoinMarketCapTicker.NO_LIMIT_REQUEST.asString();
        } catch (final UnirestException e) {
            throw new ExtractionException("Failure fetching data from coinmarketcap.", e);
        }
    }

    private static Set<CoinMarketCapTicker> deserialize(final HttpResponse<String> response) {
        return GSON.fromJson(response.getBody(), new
                TypeToken<Set<CoinMarketCapTicker>>() {
                }.getType());
    }

    private static List<CsvCoin.Builder> builders(final Collection<CoinMarketCapTicker> coins) {
        return coins.stream().map(CsvCoin::builderFromCoin).collect(ImmutableList.toImmutableList());
    }

    private static void suplementWithCryptoCompareData(final List<CsvCoin.Builder> builders) {
        try {
            Coins.getCoinList().coins.values().forEach(c -> fillWithSnapshotData(builders, c));
        } catch (IOException | OutOfCallsException e) {
            throw new ExtractionException("Error fetching data from cryptocompare.", e);
        }
    }

    private static CsvCoin.Builder fillBuilder(final Coins.CoinSnapshot.Data.General generalData,
                                               final CsvCoin.Builder coinBuilder) {
        return coinBuilder.withAlgorithm(generalData.algorithm) //
                .withProofType(generalData.proofType) //
                .withStartDate(generalData.startDate) //
                .withWebsiteUrl(generalData.url) //
                .withName(generalData.name);
    }

    private static Optional<CsvCoin.Builder> matchSymbolAndName(final List<CsvCoin.Builder> builders,
                                                                final Coins.CoinList.CoinEntry entry) {
        return builders.stream().filter(b -> b.matchesName(entry.coinName) && b
                .matchesSymbol(entry.symbol)).findFirst();
    }

    private static Optional<CsvCoin.Builder> matchSymbol(final List<CsvCoin.Builder> builders,
                                                         final Coins.CoinList.CoinEntry entry) {
        return builders.stream().filter(b -> {
            boolean match = b.matchesSymbol(entry.symbol);
            if (match) {
                System.out.println("Only symbol match for " + entry.symbol + ": name=" + entry.coinName +
                        "/" + b.getName());
            }
            return match;
        }).findFirst();
    }

    private static void fillWithSnapshotData(final List<CsvCoin.Builder> builders, final Coins.CoinList
            .CoinEntry c) {
        try {
            final Coins.CoinSnapshot snapshot = Coins.getCoinSnapshot(c.id);
            final Coins.CoinSnapshot.Data.General generalData = snapshot.data.general;
            final CsvCoin.Builder coinBuilder = matchSymbolAndName(builders, c).orElseGet(() -> matchSymbol
                    (builders, c).orElse(null));

            if (coinBuilder != null) {
                fillBuilder(generalData, coinBuilder);
            } else {
                System.out.println("Could not find match for " + c.coinName + " with symbol " + c.symbol);
            }
        } catch (IOException | OutOfCallsException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String[]> toCsvList(final Collection<CsvCoin.Builder> coinBuilders) {
        final ImmutableList.Builder<String[]> builder = ImmutableList.builder();
        builder.add(CsvCoin.headerRow());
        coinBuilders.forEach(b -> builder.add(b.build().getRow()));
        return builder.build();
    }
}

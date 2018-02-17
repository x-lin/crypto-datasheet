package tracker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import me.joshmcfarlin.CryptoCompareAPI.Coins;
import me.joshmcfarlin.CryptoCompareAPI.Utils.OutOfCallsException;
import tracker.schema.coinmarketcap.CoinMarketCapTicker;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author LinX
 */
public class Main {
    private static final Path OUTPUT_FILEPATH = Paths.get("coins.csv");

    private static final Gson GSON = new Gson();

    public static void main(final String[] args) throws UnirestException, IOException, OutOfCallsException {
        final HttpResponse<String> response = CoinMarketCapTicker.NO_LIMIT_REQUEST.asString();
        final Set<CoinMarketCapTicker> coins = convert(response, new TypeToken<Set<CoinMarketCapTicker>>() {
        });

        final FileWriter writer = new FileWriter(OUTPUT_FILEPATH);
        final List<CsvCoin.Builder> builders = CsvCoins.builders(coins);

        final Coins.CoinList coinList = Coins.getCoinList();
        System.out.println(coinList.coins.values());
        coinList.coins.values().forEach(c -> {
            try {
                final Coins.CoinSnapshot snapshot = Coins.getCoinSnapshot(c.id);
                final Coins.CoinSnapshot.Data.General generalData = snapshot.data.general;
                final CsvCoin.Builder coinBuilder = builders.stream().filter(b -> b.matchesName(c.coinName) && b
                        .matchesSymbol(c.symbol)).findFirst().orElseGet(() -> matchSymbol(builders, c).orElse(null));


                if (coinBuilder != null) {
                    coinBuilder.withAlgorithm(generalData.algorithm) //
                            .withProofType(generalData.proofType) //
                            .withStartDate(generalData.startDate) //
                            .withWebsiteUrl(generalData.url) //
                            .withName(generalData.name);
                } else {
                    System.out.println("Could not find match for " + c.coinName + " with symbol " + c.symbol);
                }
            } catch (IOException | OutOfCallsException e) {
                throw new RuntimeException(e);
            }
        });

        writer.writeCsv(CsvCoins.toCsvList(builders));
    }

    private static Optional<CsvCoin.Builder> matchSymbol(final List<CsvCoin.Builder> builders, final Coins.CoinList
            .CoinEntry c) {
        return builders.stream().filter(b -> {
            boolean match = b.matchesSymbol(c.symbol);
            if (match) {
                System.out.println("Only symbol match for " + c.symbol + ": name=" + c.coinName +
                        "/" + b.getName());
            }
            return match;
        }).findFirst();
    }

    public static <T> T convert(final HttpResponse<String> response, final TypeToken<T> typeToken) {
        return GSON.fromJson(response.getBody(), typeToken.getType());
    }
}

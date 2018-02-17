package tracker;

import com.google.common.collect.ImmutableList;
import tracker.schema.coinmarketcap.CoinMarketCapTicker;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author LinX
 */
public class CsvCoin {
    private static final DecimalFormat TWO_PLACES_DEC_FORMAT = new DecimalFormat("#.##");

    private static final List<ColumnSpec> COLUMN_SPEC = ImmutableList.of( //
            ColumnSpec.column("symbol", c -> c.symbol), //
            ColumnSpec.column("name", c -> c.name), //
            ColumnSpec.column("priceUsd", c -> c.priceUsd), //
            ColumnSpec.column("startDate", c -> c.startDate), //
            ColumnSpec.column("proofType", c -> c.proofType), //
            ColumnSpec.column("algorithm", c -> c.algorithm), //
            ColumnSpec.column("websiteUrl", c -> c.websiteUrl), //
            ColumnSpec.column("marketCapUsd", c -> c.marketCapUsd), //
            ColumnSpec.column("availableSupply", c -> c.availableSupply), //
            ColumnSpec.column("totalSupply", c -> c.totalSupply), //
            ColumnSpec.column("percentAvailable", c -> c.percentSupplyAvailable), //
            ColumnSpec.column("volumeLast24hUsd", c -> c.volumeLast24hUsd), //
            ColumnSpec.column("percentChange24h", c -> c.percentChangeLast24h), //
            ColumnSpec.column("percentChange7d", c -> c.percentChangeLast7d)
    );

    private final String symbol;

    private final String priceUsd;

    private final String volumeLast24hUsd;

    private final String marketCapUsd;

    private final String availableSupply;

    private final String totalSupply;

    private final String percentSupplyAvailable;

    private final String percentChangeLast24h;

    private final String percentChangeLast7d;

    private final String proofType;

    private final String name;

    private final String algorithm;

    private final String websiteUrl;

    private final String startDate;

    private CsvCoin(final Builder builder) {
        this.symbol = builder.symbol;
        this.priceUsd = doubleToString(builder.priceUsd);
        this.volumeLast24hUsd = doubleToString(builder.volumeLast24hUsd);
        this.marketCapUsd = doubleToString(builder.marketCapUsd);
        this.availableSupply = doubleToString(builder.availableSupply);
        this.totalSupply = doubleToString(builder.totalSupply);
        this.percentChangeLast24h = doubleToString(builder.percentChangeLast24h);
        this.percentChangeLast7d = doubleToString(builder.percentChangeLast7d);
        this.percentSupplyAvailable = calcPercentage(builder.availableSupply, builder.totalSupply);
        this.proofType = builder.proofType;
        this.name = builder.name;
        this.algorithm = builder.algorithm;
        this.websiteUrl = builder.websiteUrl;
        this.startDate = builder.startDate;
    }

    String[] getRow() {
        return COLUMN_SPEC.stream().map(c -> c.columnValuePicker.apply(this)).toArray(String[]::new);
    }

    static String[] headerRow() {
        return COLUMN_SPEC.stream().map(c -> c.headerName).toArray(String[]::new);
    }

    public static CsvCoin.Builder builderFromCoin(final CoinMarketCapTicker coin) {
        return new Builder(coin.getSymbol())
                .withName(coin.getId())
                .withMarketCapUsd(coin.getMarketCapUsd())
                .withPercentChangeLast7d(coin.getPercentChangeLast7d())
                .withPercentChangeLast24h(coin.getPercentChangeLast24h())
                .withPriceUsd(coin.getPriceUsd())
                .withAvailableSupply(coin.getAvailableSupply())
                .withTotalSupply(coin.getTotalSupply())
                .withVolumeLast24hUsd(coin.getVolumeLast24hUsd());
    }

    private static String calcPercentage(final Double part, final Double whole) {
        return part != null && whole != null ? String.valueOf(TWO_PLACES_DEC_FORMAT.format(part / whole * 100)) : null;
    }

    private static String doubleToString(final Double value) {
        return value != null ? String.valueOf(TWO_PLACES_DEC_FORMAT.format(value)) : null;
    }

    private static class ColumnSpec {
        private final String headerName;

        private final Function<CsvCoin, String> columnValuePicker;

        private ColumnSpec(final String headerName, final Function<CsvCoin, String> columnValuePicker) {
            this.headerName = headerName;
            this.columnValuePicker = columnValuePicker;
        }

        private static ColumnSpec column(final String headerName, final Function<CsvCoin, String> columnValuePicker) {
            return new ColumnSpec(headerName, columnValuePicker);
        }
    }

    public static class Builder {
        private final String symbol;

        private Double priceUsd = null;

        private Double volumeLast24hUsd = null;

        private Double marketCapUsd = null;

        private Double availableSupply = null;

        private Double totalSupply = null;

        private Double percentChangeLast24h = null;

        private Double percentChangeLast7d = null;

        private String proofType = null;

        private String name = null;

        private String algorithm = null;

        private String websiteUrl = null;

        private String startDate = null;

        private Builder(final String symbol) {
            this.symbol = symbol;
        }

        public Builder withPriceUsd(final Double price) {
            this.priceUsd = price;
            return this;
        }

        public Builder withVolumeLast24hUsd(final Double volume) {
            this.volumeLast24hUsd = volume;
            return this;
        }

        public Builder withMarketCapUsd(final Double marketCap) {
            this.marketCapUsd = marketCap;
            return this;
        }

        public Builder withAvailableSupply(final Double supply) {
            this.availableSupply = supply;
            return this;
        }

        public Builder withTotalSupply(final Double supply) {
            this.totalSupply = supply;
            return this;
        }

        public Builder withPercentChangeLast24h(final Double change) {
            this.percentChangeLast24h = change;
            return this;
        }

        public Builder withPercentChangeLast7d(final Double change) {
            this.percentChangeLast7d = change;
            return this;
        }

        public Builder withProofType(final String proofType) {
            this.proofType = proofType;
            return this;
        }

        public Builder withAlgorithm(final String algorithm) {
            this.algorithm = algorithm;
            return this;
        }

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Builder withWebsiteUrl(final String url) {
            this.websiteUrl = url;
            return this;
        }

        public Builder withStartDate(final String startDate) {
            this.startDate = startDate;
            return this;
        }

        public boolean matchesName(final String name) {
            return Objects.equals(trimString(this.name), trimString(name));
        }

        public boolean matchesSymbol(final String symbol) {
            return Objects.equals(this.symbol.toLowerCase().trim(), symbol.toLowerCase().trim());
        }

        CsvCoin build() {
            return new CsvCoin(this);
        }

        public String getName() {
            return this.name;
        }

        private static String trimString(final String original) {
            return original.toLowerCase().trim().replace(" ", "");
        }
    }
}

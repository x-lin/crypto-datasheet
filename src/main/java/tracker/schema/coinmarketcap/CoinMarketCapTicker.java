package tracker.schema.coinmarketcap;

import com.google.gson.annotations.SerializedName;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;

import java.util.Objects;

/**
 * @author LinX
 */
public class CoinMarketCapTicker {
    public static final HttpRequest NO_LIMIT_REQUEST = Unirest.get("https://api.coinmarketcap.com/v1/ticker/")
            .queryString("limit", "0");

    private String id;

    private String symbol;

    @SerializedName("price_usd")
    private Double priceUsd;

    @SerializedName("price_btc")
    private Double priceBtc;

    @SerializedName("24h_volume_usd")
    private Double volumeLast24hUsd;

    @SerializedName("market_cap_usd")
    private Double marketCapUsd;

    @SerializedName("available_supply")
    private Double availableSupply;

    @SerializedName("total_supply")
    private Double totalSupply;

    @SerializedName("percent_change_24h")
    private Double percentChangeLast24h;

    @SerializedName("percent_change_7d")
    private Double percentChangeLast7d;

    public String getId() {
        return this.id;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public Double getPriceUsd() {
        return this.priceUsd;
    }

    public Double getPriceBtc() {
        return this.priceBtc;
    }

    public Double getVolumeLast24hUsd() {
        return this.volumeLast24hUsd;
    }

    public Double getMarketCapUsd() {
        return this.marketCapUsd;
    }

    public Double getAvailableSupply() {
        return this.availableSupply;
    }

    public Double getTotalSupply() {
        return this.totalSupply;
    }

    public Double getPercentChangeLast24h() {
        return this.percentChangeLast24h;
    }

    public Double getPercentChangeLast7d() {
        return this.percentChangeLast7d;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CoinMarketCapTicker coin = (CoinMarketCapTicker) o;
        return Double.compare(coin.priceUsd, this.priceUsd) == 0 &&
                Double.compare(coin.priceBtc, this.priceBtc) == 0 &&
                Double.compare(coin.volumeLast24hUsd, this.volumeLast24hUsd) == 0 &&
                Double.compare(coin.marketCapUsd, this.marketCapUsd) == 0 &&
                Double.compare(coin.availableSupply, this.availableSupply) == 0 &&
                Double.compare(coin.totalSupply, this.totalSupply) == 0 &&
                Double.compare(coin.percentChangeLast24h, this.percentChangeLast24h) == 0 &&
                Double.compare(coin.percentChangeLast7d, this.percentChangeLast7d) == 0 &&
                Objects.equals(this.id, coin.id) &&
                Objects.equals(this.symbol, coin.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.symbol, this.priceUsd, this.priceBtc, this.volumeLast24hUsd, this
                .marketCapUsd, this.availableSupply, this.totalSupply, this.percentChangeLast24h, this
                .percentChangeLast7d);
    }

    @Override
    public String toString() {
        return "CoinMarketCapTicker{" +
                "id='" + this.id + '\'' +
                ", symbol='" + this.symbol + '\'' +
                ", priceUsd=" + this.priceUsd +
                ", priceBtc=" + this.priceBtc +
                ", volumeLast24hUsd=" + this.volumeLast24hUsd +
                ", marketCapUsd=" + this.marketCapUsd +
                ", availableSupply=" + this.availableSupply +
                ", totalSupply=" + this.totalSupply +
                ", percentChangeLast24h=" + this.percentChangeLast24h +
                ", percentChangeLast7d=" + this.percentChangeLast7d +
                '}';
    }
}

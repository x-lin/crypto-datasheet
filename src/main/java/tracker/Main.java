package tracker;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author LinX
 */
public class Main {
    private static final Path OUTPUT_FILEPATH = Paths.get("coins.csv");

    public static void main(final String[] args) {
        final List<String[]> data = CsvCoins.fetchCoinDataAsCsv();
        final FileWriter writer = new FileWriter(OUTPUT_FILEPATH);
        writer.writeCsv(data);
    }
}

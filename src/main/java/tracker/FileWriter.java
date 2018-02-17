package tracker;

import com.opencsv.CSVWriter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author LinX
 */
public class FileWriter {
    private final CSVWriter csvWriter;

    public FileWriter(final Path filePath) {
        try {
            this.csvWriter = new CSVWriter(Files.newBufferedWriter(filePath, StandardCharsets.UTF_8), ';',
                    CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void writeCsv(final List<String[]> data) {
        this.csvWriter.writeAll(data, true);
    }
}

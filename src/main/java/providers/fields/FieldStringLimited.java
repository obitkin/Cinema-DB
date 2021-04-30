package providers.fields;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

public class FieldStringLimited {

    Random random = new Random();

    private List<String> lines;
    private int current = 0;
    private int length;

    FieldStringLimited(Path path, int length) throws IOException {
        this.length = length;
        lines = Files.readAllLines(path);
    }

    public String getCurrent() {
        return lines.get(current++);
    }

    public int setCurrent(int newValue) {
        int tmp = current;
        current = newValue;
        return tmp;
    }

    public void flush() {
        setCurrent(0);
    }

    public String getRandom() {
        return lines.get(random.nextInt(lines.size()));
    }
}

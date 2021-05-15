package providers.fields;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class FieldStringRandom {

    Random random = new Random();

    private final List<List<String>> lines;

    private int length;
    private int size;

    public FieldStringRandom(Path[] paths, int length) throws IOException {
        this.size = paths.length;
        this.length = length;
        lines = Arrays.stream(paths)
                .map(path -> {
                    try {
                        return Files.readAllLines(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
        if (lines.contains(null)) {
            throw new IOException();
        }
    }

    public String getRandom() {
        String res;
        do {
            List<String> result = new ArrayList<>();
            for (List<String> file : lines) {
                result.add(getRandom(file));
            }
            res = concat(result);
        } while (res.length() > length);
        return res;
    }

    private String getRandom(List<String> list) {
        return list.get(random.nextInt(list.size()));
    }

    private String concat(List<String> list) {
        return list
                .stream()
                .reduce((s1, s2) -> s1 + " " + s2)
                .get();
    }

}

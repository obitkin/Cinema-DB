import java.util.Random;
import java.util.function.Supplier;

public class SupplierMy implements Supplier<String> {

    static Random random = new Random();

    String[] data;

    SupplierMy(String[] data) {
        this.data = data;
    }

    @Override
    public String get() {
        return data[random.nextInt(data.length)];
    }
}

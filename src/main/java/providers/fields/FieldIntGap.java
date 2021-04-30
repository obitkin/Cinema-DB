package providers.fields;

import java.util.Random;

public class FieldIntGap {

    Random random = new Random();

    private int current = 1;
    private final int max;

    FieldIntGap(int max) {
        this.max = max;
    }

    public int getCurrent() {
        return current++;
    }

    public int setCurrent(int newValue) {
        int tmp = current;
        current = newValue;
        return tmp;
    }

    public void flush() {
        setCurrent(1);
    }

    public int getMax() {
        return max;
    }

    public int getRandom() {
        return random.nextInt(max);
    }

}

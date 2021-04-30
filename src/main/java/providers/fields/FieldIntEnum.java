package providers.fields;

import java.util.Random;

public class FieldIntEnum {

    Random random = new Random();

    private final int[] arr;

    public FieldIntEnum(int... arr) {
        this.arr = arr;
    }

    public int getRandom() {
        return arr[random.nextInt(arr.length)];
    }

}

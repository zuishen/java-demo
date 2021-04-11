package work.jimmmy.java.core.collections;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class IteratorDemo {
    public static void main(String[] args) {
        iteratorDemo();
    }

    private static void iteratorDemo() {
        List<String> list = Arrays.asList("aa", "bb", "cc");
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String elem = iterator.next();
            System.out.println(elem);
        }
    }
}

package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Collections;
import java.util.List;

public class TestHelpers {

    /**
     * Asserts that two lists are equal ignoring the order of the elements
     * Source: 2511 Blackout Assignment Source Code
     * @param <T> the type of the lists
     * @param a the first list
     * @param b the second list
     */
    public static<T extends Comparable<? super T>> void assertListAreEqualIgnoringOrder(List<T> a, List<T> b) {
        Collections.sort(a);
        Collections.sort(b);
        assertArrayEquals(a.toArray(), b.toArray());
    }
}

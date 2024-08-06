// TabooTest.java
// Taboo class tests -- nothing provided.

import java.util.*;

import junit.framework.TestCase;

public class TabooTest extends TestCase {
    public void testNoFollow1() {
        List<String> rules = Arrays.asList("a", "b", "c", "a", "c", "a", "e", "a", null, "l");
        Taboo<String> taboo = new Taboo<>(rules);

        Set<String> s1 = new HashSet<>(Arrays.asList("b", "c", "e"));
        assertEquals(s1, taboo.noFollow("a"));
        Set<String> s2 = new HashSet<>(List.of("a"));
        assertEquals(s2, taboo.noFollow("c"));
        Set<String> s3 = new HashSet<>(List.of("a"));
        assertEquals(s3, taboo.noFollow("e"));
    }

    public void testNoFollow2() {
        List<String> rules = Arrays.asList("a", "c", "a", "b");
        Taboo<String> taboo = new Taboo<>(rules);

        Set<String> s1 = new HashSet<>();
        assertEquals(s1, taboo.noFollow("x"));
        Set<String> s2 = new HashSet<>(Arrays.asList("c", "b"));
        assertEquals(s2, taboo.noFollow("a"));
    }

    public void testReduce1() {
        List<String> rules = Arrays.asList("a", "c", "a", "b");
        Taboo<String> taboo = new Taboo<>(rules);

        List<String> list = new ArrayList<>(Arrays.asList("a", "c", "b", "x", "c", "a"));
        List<String> result = new ArrayList<>(Arrays.asList("a", "x", "c"));
        taboo.reduce(list);
        assertEquals(result, list);
    }

    public void testReduce2() {
        List<String> rules = Arrays.asList("a", "a", "b", null, "c");
        Taboo<String> taboo = new Taboo<>(rules);

        List<String> list = new ArrayList<>(Arrays.asList("a", "a", "a", "x", "b", "c"));
        List<String> result = new ArrayList<>(Arrays.asList("a", "x", "b", "c"));
        taboo.reduce(list);
        assertEquals(result, list);
    }

    public void testReduce3() {
        List<String> rules = new ArrayList<>();
        Taboo<String> taboo = new Taboo<>(rules);

        List<String> list = new ArrayList<>(Arrays.asList("a", "a", "a", "x", "b", "c"));
        List<String> result = new ArrayList<>(Arrays.asList("a", "a", "a", "x", "b", "c"));
        taboo.reduce(list);
        assertEquals(result, list);
    }

    public void testReduce4() {
        List<String> rules = Arrays.asList("a", "a");
        Taboo<String> taboo = new Taboo<>(rules);

        List<String> list = new ArrayList<>(Arrays.asList("a", "a", "a", "a", "a", "a"));
        List<String> result = new ArrayList<>(Arrays.asList("a"));
        taboo.reduce(list);
        assertEquals(result, list);
    }

    public void testReduce5() {
        List<String> rules = Arrays.asList("a", "a", "b");
        Taboo<String> taboo = new Taboo<>(rules);

        List<String> list = new ArrayList<>(Arrays.asList("a", "a", "a", "b", "b", "a"));
        List<String> result = new ArrayList<>(Arrays.asList("a"));
        taboo.reduce(list);
        assertEquals(result, list);
    }
}

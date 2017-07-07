package option;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class OptionalExample {

    public static <T1, T2, R> Optional<R> zipMap(Optional<T1> o1, Optional<T2> o2, BiFunction<T1, T2, R> f) {
        return o1.flatMap(t1 -> o2.map(t2 -> f.apply(t1, t2)));
    }

    @Test
    public void zipMapWithoutNullValues() {

        final Optional<String> s1 = Optional.of("1");
        final Optional<String> s2 = Optional.of("2");

        final Optional<Map<String, String>> expected =
                Optional.of(Collections.singletonMap(s1.get(), s2.get()));

        final Optional<Map<String, String>> actual =
                zipMap(s1, s2, Collections::singletonMap);

        assertEquals(expected, actual);
    }

    @Test
    public void zipMapWithNullValues() {

        final Optional<String> s1 = Optional.ofNullable(null);
        final Optional<String> s2 = Optional.of("2");

        final Optional<Object> expected = Optional.empty();

        final Optional<Map<String, String>> actual =
                zipMap(s1, s2, Collections::singletonMap);

        assertEquals(expected, actual);
    }

    @Test
    public void get() {
        final Optional<String> o1 = Optional.empty();

        o1.ifPresent(s -> assertEquals("abc", s));

        assertEquals("t", o1.orElse("t"));
        assertEquals("t", o1.orElseGet(() -> "t"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void get2() {
        final Optional<String> o1 = Optional.empty();

        o1.orElseThrow(() -> new UnsupportedOperationException());
    }

    @Test
    public void ifPresent() {
        final Optional<String> o1 = getOptional();

        o1.ifPresent(s -> assertEquals("abc", s));

        if (o1.isPresent()) {
            assertEquals("abc", o1.get());
        }
    }

    @Test
    public void map() {
        final Optional<String> o1 = getOptional();

        final Function<String, Integer> getLength = String::length;

        final Optional<Integer> expected = o1.map(getLength);

        final Optional<Integer> actual;
        if (o1.isPresent()) {
            actual = Optional.of(getLength.apply(o1.get()));
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void flatMap() {
        final Optional<String> o1 = getOptional();

        final Optional<Integer> expected = o1.flatMap(s -> Optional.of(s.length()));

        final Optional<Integer> actual;
        if (o1.isPresent()) {
            actual = Optional.of(o1.get().length());
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void filter() {
        final Optional<String> o1 = getOptional();

        o1.filter(s -> s.length() >= 3)
                .ifPresent(s -> assertEquals("abc", s));

        final Optional<String> actual;
        if (o1.isPresent()) {
            actual = Optional.of(o1.get()).filter(s -> s.length() >= 3);
            assertEquals("abc", actual.get());
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void filter2() {
        final Optional<String> o1 = getOptional();

        o1.filter(s -> s.length() >= 4)
                .get();
    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
                ? Optional.empty()
                : Optional.of("abc");
    }
}

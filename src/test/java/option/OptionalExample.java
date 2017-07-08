package option;

import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class OptionalExample {

    public static <T1, T2, R> Optional<R> zipMap(Optional<T1> o1, Optional<T2> o2, BiFunction<T1, T2, R> function) {
        return o1.flatMap(t1 ->
                o2.map(t2 ->
                   function.apply(t1, t2)));
    }

    @Test
    public void zipMap() {
        final Optional<String> o1 = getOptional();
        final Optional<String> o2 = getOptional();

        BiFunction<String, String, Integer> function = (s1, s2) -> s1.length() + s2.length();

        Optional<Integer> expected = zipMap(o1, o2, function);

        Optional<Integer> actual;

        if (o1.isPresent() && o2.isPresent()) {
            actual = Optional.ofNullable(function.apply(o1.get(), o2.get()));
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void get() {
        final Optional<String> o1 = Optional.empty();

        o1.ifPresent(s -> System.out.println(s));

        o1.orElse("t");
        o1.orElseGet(() -> "t");
        o1.orElseThrow(() -> new UnsupportedOperationException());
    }

    @Test
    public void ifPresent() {
        final Optional<String> o1 = getOptional();

        o1.ifPresent(System.out::println);

        if (o1.isPresent()) {
            System.out.println(o1.get());
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
        final Optional<String> strOptional = getOptional();

        final Function<String, Optional<Integer>> getLength = s -> Optional.of(s.length());

        final Optional<Integer> expected = strOptional.flatMap(getLength);

        final Optional<Integer> actual;
        if (strOptional.isPresent()) {
            actual = getLength.apply(strOptional.get());
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void orElseThrow() {
        final Optional<String> strOptional = getOptional();

        boolean expectedIsEmpty = false;
        try {
            strOptional.orElseThrow(() -> new RuntimeException());
        } catch (RuntimeException e) {
            expectedIsEmpty = true;
        }

        boolean actualIsEmpty = !strOptional.isPresent();

        assertEquals(expectedIsEmpty, actualIsEmpty);
    }

    @Test
    public void orElse() {
        final Optional<String> strOptional = getOptional();

        String expected = strOptional.orElse("test");

        String actual;
        if (strOptional.isPresent()) {
            actual = "abc";
        } else {
            actual = "test";
        }

        assertEquals(expected, actual);
    }

    @Test
    public void orElseGet() {
        final Optional<String> strOptional = getOptional();

        String expected = strOptional.orElseGet(() -> "test");

        String actual;
        if (strOptional.isPresent()) {
            actual = strOptional.get();
        } else {
            actual = "test";
        }

        assertEquals(expected, actual);
    }

    @Test
    public void filter() {
        final Optional<String> strOptional = getOptional();

        Optional<String> expected = strOptional.filter(t -> t.equals("abc"));

        Optional<String> actual = strOptional;

        assertEquals(expected, actual);
    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
            ? Optional.empty()
            : Optional.of("abc");
    }
}

package option;

import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;

public class OptionalExample {

    @Test
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

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
            ? Optional.empty()
            : Optional.of("abc");
    }

    @Test
    public void filter() {
        final Optional<String> o1 = getOptional();
        final Predicate<String> checkLengthPredicate = s -> s.length() > 2;
        final Optional<String> expected = o1.filter(checkLengthPredicate);
        final Optional<String> actual;
        if (o1.isPresent() && checkLengthPredicate.test(o1.get())) {
            actual = o1;
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void flatMap() {
        final Optional<String> o1 = getOptional();
        final Function<String, Optional<Integer>> getLength = s -> Optional.of(s.length());
        final Optional<Integer> expected = o1.flatMap(getLength);
        final Optional<Integer> actual;
        if (o1.isPresent()) {
            actual = getLength.apply(o1.get());
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void orElse() {
        String defaultString = "default";
        final Optional<String> o1 = getOptional();
        final String expected = o1.orElse(defaultString);
        final String actual;
        if (o1.isPresent()) {
            actual = o1.get();
        } else {
            actual = defaultString;
        }

        assertEquals(expected, actual);
    }

/*
Homework:
zipMap(joins two elements via some Function)
public static<T1, T2, R> Optional<R> zipMap(Optional<T1> o1, Optional<T2> o2, BiFunction<T1, T2, R> f) {
        (use map and flatmap, don;t use isPresent() and get())
        }*/

     private static<T1, T2, R> Optional<R> zipMap(Optional<T1> o1, Optional<T2> o2, BiFunction<T1, T2, R> f) {
        return o1.flatMap(t1 ->
                    o2.map(t2 ->
                        f.apply(t1, t2)));
    }

    @Test
    public void zipMap() {
        final Optional<String> o1 = getOptional();
        final Optional<String> o2 = getOptional();

        BiFunction<String, String, Integer> lengthSum = (s1, s2) -> s1.length() + s2.length();

        Optional<Integer> expected = zipMap(o1, o2, lengthSum);
        Optional<Integer> actual;

        if (o1.isPresent() && o2.isPresent()) {
            actual = Optional.of(lengthSum.apply(o1.get(), o2.get()));
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }
}



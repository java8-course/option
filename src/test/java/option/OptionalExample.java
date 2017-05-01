package option;

import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
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

    @Test
    public void testFilter() {
        final Optional<String> o1 = getOptional();

        final Predicate<String> p = String::isEmpty;

        final Optional<String> expected = o1.filter(p);

        final Optional<String> actual;
        if (o1.isPresent() && p.test(o1.get())) {
            actual = Optional.of(o1.get());
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testOrElse() {
        final Optional<String> o1 = getOptional();

        final String expected = o1.orElse("else");

        final String actual;
        if (o1.isPresent()) {
            actual = o1.get();
        } else {
            actual = "else";
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testFlatMap() {
        final Optional<String> o1 = getOptional();

        final Function<String, Optional<String>> function = Optional::of;

        final Optional<String> expected = o1.flatMap(function);

        final Optional<String> actual;
        if (o1.isPresent()) {
            actual = function.apply(o1.get());
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
}
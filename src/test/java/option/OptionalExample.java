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
    public void flatMap() {
        final Optional<String> optional = getOptional();

        final Function<String, Optional<Integer>> getLength = s -> Optional.of(s.length());

        final Optional<Integer> expected = optional.flatMap(getLength);

        final Optional<Integer> actual;

        if (optional.isPresent()) {
            actual = getLength.apply(optional.get());
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void filter() {
        final Optional<String> optional = getOptional();

        final Predicate<String> predicate = String::isEmpty;

        final Optional<String> expected = optional.filter(predicate);

        final Optional<String> actual;

        if (optional.isPresent() && predicate.test(optional.get())) {
            actual = optional;
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void orElse() {
        final Optional<String> optional = getOptional();

        final String param = "defg";

        final String expected = optional.orElse(param);

        final String actual;

        if (optional.isPresent()) {
            actual = optional.get();
        } else {
            actual = param;
        }

        assertEquals(expected, actual);
    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
                ? Optional.empty()
                : Optional.of("abc");
    }
}

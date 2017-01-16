package option;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;

public class OptionalExample {

    @Test(expected = UnsupportedOperationException.class)
    public void get() {
        final Optional<String> o1 = Optional.empty();

        o1.ifPresent(System.out::println);

        o1.orElse("t");
        o1.orElseGet(() -> "t");
        o1.orElseThrow(UnsupportedOperationException::new);
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
    public void flatMap() {
        final Optional<String> o1 = getOptional();

        final Function<String, Optional<Integer>> getOptionalLength = s -> Optional.of(s.length());

        final Optional<Integer> expected = o1.flatMap(getOptionalLength);

        final Optional<Integer> actual;
        if (o1.isPresent()) {
            actual = getOptionalLength.apply(o1.get());
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void filter() {
        final Optional<String> o1 = getOptional();

        final Predicate<String> checkLength = s -> s.length() == 3;
        final Predicate<String> checkLength1 = s -> s.length() == 4;

        final Optional<String> expected = o1.filter(checkLength);
        final Optional<String> expected1 = o1.filter(checkLength1);

        final Optional<String> actual;
        final Optional<String> actual1;
        if (o1.isPresent()) {
            actual = checkLength.test(o1.get()) ? o1 : Optional.empty();
            actual1 = checkLength1.test(o1.get()) ? o1 : Optional.empty();
        } else {
            actual = Optional.empty();
            actual1 = Optional.empty();
        }

        assertEquals(expected, actual);
        assertEquals(expected1, actual1);
    }

    @Test
    public void orElse() {
        final Optional<String> o1 = getOptional();

        final String expected = o1.orElse("test");

        final String actual;
        if (o1.isPresent()) {
            actual = o1.get();
        } else {
            actual = "test";
        }

        assertEquals(expected, actual);
    }
}

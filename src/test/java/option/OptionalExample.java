package option;

import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class OptionalExample {

    private static final String other = "other";
    private static final String ABC = "abc";

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
    public void orElse() {
        Optional<String> o1 = getOptional();

        String expected = o1.orElse(other);

        String actual = o1.isPresent() ? o1.get() : other;

        assertEquals(expected, actual);
    }

    @Test
    public void orElseGet() {
        Optional<String> o1 = getOptional();

        String expected = o1.orElseGet(() -> other);

        String actual = o1.isPresent() ? o1.get() : other;

        assertEquals(expected, actual);
    }

    @Test
    public void filter() {
        Optional<String> o1 = getOptional();

        Optional<String> expected = Optional.of(ABC);

        Optional<String> actual = o1.filter(s -> s.equals(ABC));

        if (!o1.isPresent()) {
            expected = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void flatMap() {
        Optional<String> o1 = getOptional();

        Function<String, Optional<String>> firstChar = s -> Optional.of(s.substring(0, 1));

        Optional<String> expected = o1.flatMap(firstChar);

        Optional<String> actual;

        if (o1.isPresent()) {
            actual = firstChar.apply(o1.get());
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
                ? Optional.empty()
                : Optional.of(ABC);
    }
}

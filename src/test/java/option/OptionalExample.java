package option;

import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;

public class OptionalExample {

    @Test
    public void get () {
        final Optional<String> o1 = Optional.empty();

        o1.ifPresent(System.out::println);

        o1.orElse("t");
        o1.orElseGet(() -> "t");
//        o1.orElseThrow(() -> new UnsupportedOperationException());
    }

    @Test
    public void ifPresent () {
        final Optional<String> o1 = getOptional();

        o1.ifPresent(System.out::println);

        if (o1.isPresent()) {
            System.out.println(o1.get());
        }
    }

    @Test
    public void map () {
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
    public void filter () {
        final Optional<String> o1 = getOptional();
        final Optional<String> actual;
        final Predicate<String> p = s -> s.contains("a");
        final Optional<String> expected = o1.filter(p);

        if (o1.isPresent()) {
            if (p.test(o1.get())) {
                actual = o1;
            } else {
                actual = Optional.empty();
            }
        } else {
            actual = Optional.empty();
        }
        assertEquals(expected, actual);
    }

    @Test
    public void flatMap () {
        final Optional<String> o1 = getOptional();
        final Optional<String> actual;
        final Function<String, Optional<String>> replace = s -> (s == null) ? Optional.empty() : Optional.of(s.replace("abc", "cba"));
        final Optional<String> expected = o1.flatMap(replace);
        if (o1.isPresent())
            actual = replace.apply(o1.get());
        else
            actual = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    public void orElse () {
        final Optional<String> o1 = getOptional();
        final String actual;
        final String defaultVal = "xyz";
        final String expected = o1.orElse(defaultVal);
        if (o1.isPresent()) {
            actual = o1.get();
        } else {
            actual = defaultVal;
        }
        assertEquals(expected, actual);
    }

    private Optional<String> getOptional () {
        return ThreadLocalRandom.current().nextBoolean()
                ? Optional.empty()
                : Optional.of("abc");
    }
}
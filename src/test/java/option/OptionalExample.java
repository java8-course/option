package option;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OptionalExample {

    private static Optional<String> myTestOpt;

    @Before
    public static void setOptional() {
        myTestOpt = ThreadLocalRandom.current().nextBoolean()
                ? Optional.empty()
                : Optional.of("abc");
    }

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
    public void filter() {
        final Predicate<String> equals = s -> s.equals("abc");
        final Optional<String> expected = myTestOpt.filter(equals);
        final Optional<String> result;

        if (myTestOpt.isPresent())
            result = equals.test(myTestOpt.get()) ? myTestOpt : Optional.empty();
        else
            result = Optional.empty();

        assertEquals(expected, result);
    }

    @Test
    public void map() {
        final Function<String, Integer> length = String::length;
        final Optional<Integer> expected = myTestOpt.map(length);
        final Optional<Integer> result;

        if (myTestOpt.isPresent())
            result = Optional.of(length.apply(myTestOpt.get()));
        else
            result = Optional.empty();

        assertEquals(expected, result);
    }

    @Test
    public void flatMap() {
        final Function<String, Optional<String>> replace = s -> (s == null) ? Optional.empty() : Optional.of(s.replace("abc", "xyz"));
        final Optional<String> expected = myTestOpt.flatMap(replace);
        final Optional<String> result;

        if (myTestOpt.isPresent())
            result = replace.apply(myTestOpt.get());
        else
            result = Optional.empty();

        assertEquals(expected, result);
    }

    @Test
    public void orElse() {
        final String defaultValue = "def";
        final String expected = myTestOpt.orElse(defaultValue);
        final String result;

        if (myTestOpt.isPresent())
            result = myTestOpt.get();
        else
            result = defaultValue;

        assertEquals(expected, result);
    }

    @Test
    public void orElseGet() {
        final Supplier<String> defaultValue = () -> "def";
        final String expected = myTestOpt.orElseGet(defaultValue);
        final String result;

        if (myTestOpt.isPresent())
            result = myTestOpt.get();
        else
            result = defaultValue.get();

        assertEquals(expected, result);
    }

    @Test
    public void orElseThrow() throws Exception {
        final Supplier<Exception> defaultValue = IllegalArgumentException::new;

        if (myTestOpt.isPresent())
            assertEquals(myTestOpt.orElseThrow(defaultValue), myTestOpt.get());
        else
            try {
                myTestOpt.orElseThrow(defaultValue);
            } catch (Throwable ex) {
                assertTrue(ex instanceof IllegalArgumentException);
            }
    }


    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
                ? Optional.empty()
                : Optional.of("abc");
    }
}

package option;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OptionalExample {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static Optional<String> o;

    @BeforeClass
    public static void setOptional() {
        o = ThreadLocalRandom.current().nextBoolean()
                ? Optional.empty()
                : Optional.of("abc");
    }

    @Test
    public void ifPresent() {
        if (o.isPresent())
            System.out.printf("Optional is %s\n", o.get());
        else
            System.out.println("Optional is empty");
    }

    @Test
    public void filter() {
        final Predicate<String> equals = s -> s.equals("abc");
        final Optional<String> expected = o.filter(equals);
        final Optional<String> result;

        if (o.isPresent())
            result = equals.test(o.get()) ? o : Optional.empty();
        else
            result = Optional.empty();

        assertEquals(expected, result);
    }

    @Test
    public void map() {
        final Function<String, Integer> length = String::length;
        final Optional<Integer> expected = o.map(length);
        final Optional<Integer> result;

        if (o.isPresent())
            result = Optional.of(length.apply(o.get()));
        else
            result = Optional.empty();

        assertEquals(expected, result);
    }

    @Test
    public void flatMap() {
        final Function<String, Optional<String>> replace = s -> (s == null) ? Optional.empty() : Optional.of(s.replace("abc", "xyz"));
        final Optional<String> expected = o.flatMap(replace);
        final Optional<String> result;

        if (o.isPresent())
            result = replace.apply(o.get());
        else
            result = Optional.empty();

        assertEquals(expected, result);
    }

    @Test
    public void orElse() {
        final String defaultValue = "def";
        final String expected = o.orElse(defaultValue);
        final String result;

        if (o.isPresent())
            result = o.get();
        else
            result = defaultValue;

        assertEquals(expected, result);
    }

    @Test
    public void orElseGet() {
        final Supplier<String> defaultValue = () -> "def";
        final String expected = o.orElseGet(defaultValue);
        final String result;

        if (o.isPresent())
            result = o.get();
        else
            result = defaultValue.get();

        assertEquals(expected, result);
    }

    @Test
    public void orElseThrow() throws Exception {
        final Supplier<Exception> defaultValue = IllegalArgumentException::new;

        if (o.isPresent())
            assertEquals(o.orElseThrow(defaultValue), o.get());
        else
            try {
                o.orElseThrow(defaultValue);
            } catch (Throwable ex) {
                assertTrue(ex instanceof IllegalArgumentException);
            }
    }

}

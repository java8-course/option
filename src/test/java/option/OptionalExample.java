package option;

import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

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

    public static <T1, T2, R> Optional<R> zipMap(Option<T1> o1, Option<T2> o2, BiFunction<T1, T2, R> function) {
        //dont use isPresent, get, mutable variables// nonstatic - use static
        //use map, flatMap
        throw new UnsupportedOperationException();
    }

    @Test
    public void orElse() {
        final Optional<String> optional = getOptional();

        final String string = "someString";

        final String expected = optional.orElse(string);

        final String actual;

        if (optional.isPresent()) {
            actual = optional.get();
        } else {
            actual = string;
        }

        assertEquals(expected, actual);
    }

    @Test
    public void orElseGet() {
        final Optional<String> optional = getOptional();

        final Supplier<String> supplier = () -> "someString";

        final String expected = optional.orElseGet(supplier);

        final String actual;

        if (optional.isPresent()) {
            actual = optional.get();
        } else {
            actual = supplier.get();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void orElseThrow() {
        final Optional<String> optional = getOptional();

        final Supplier<NullPointerException> supplier = NullPointerException::new;

        String expected = null;

        Throwable exception = new Throwable();

        try {
            expected = optional.orElseThrow(supplier);
        } catch (NullPointerException e) {
            exception = e;
        }

        String actual = null;

        try {
            if (optional.isPresent()) {
                actual = optional.get();
            } else {
                supplier.get();
            }
        } catch (NullPointerException e) {
            assertEquals(exception.getClass(), e.getClass());
        }

        assertEquals(expected, actual);
    }

    @Test
    public void filter() {
        final Optional<String> optional = getOptional();

        final Predicate<String> predicate = s -> s.equals("someString");

        Optional<String> expected = optional.filter(predicate);

        Optional<String> actual;

        if (optional.isPresent()) {
            if (predicate.test(optional.get())) {
                actual = optional;
            } else {
                actual = Optional.empty();
            }
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void flatMap() {
        final Optional<String> optional = getOptional();

        final Function<String, Optional<String>> function = s -> Optional.of("someString");

        Optional<String> expected = optional.flatMap(function);

        Optional<String> actual;

        if (optional.isPresent()) {
            actual = function.apply(optional.get());
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, expected);
    }
}
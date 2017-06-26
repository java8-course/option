package option;

import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
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
    public void filter() {
        final Optional<String> o1 = getOptional();

        final Predicate<String> isEmpty = String::isEmpty;

        final Optional<String> expected = o1.filter(isEmpty);

        final Optional<String> actual;
        if (o1.isPresent()) {
            if (isEmpty.test(o1.get())) {
                actual = Optional.of(o1.get());
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
        final Optional<String> o1 = getOptional();

        final Function<String, Optional<String>> optionalFunction = (g) -> Optional.of(g + "abc");

        final Optional<String> expected = o1.flatMap(optionalFunction);

        final Optional<String> actual;
        if (o1.isPresent()) {
            actual = optionalFunction.apply(o1.get());
        } else {
            actual = Optional.empty();
        }
        assertEquals(expected, actual);
    }

    @Test
    public void orElse() {
        final Optional<String> o1 = getOptional();

        final String abc = "abc";

        final Optional<String> expected = Optional.of(o1.orElse(abc));

        final Optional<String> actual;

        if (o1.isPresent()) {
            actual = Optional.of(o1.get());
        } else {
            actual = Optional.of(abc);
        }
        assertEquals(expected, actual);
    }

    @Test
    public void orElseGet() {
        final Optional<String> o1 = getOptional();

        final Supplier<String> supplier = () -> "abc";

        final Optional<String> expected = Optional.of(o1.orElseGet(supplier));

        final Optional<String> actual;

        if (o1.isPresent()) {
            actual = Optional.of(o1.get());
        } else {
            actual = Optional.of(supplier.get());
        }
        assertEquals(expected, actual);
    }

    @Test
    public void orElseThrow() {
        final Optional<String> o1 = getOptional();

        final Supplier<RuntimeException> supplier = () -> new NullPointerException("Another NPE");
        try {
            final Optional<String> expected = Optional.of(o1.orElseThrow(supplier));

            final Optional<String> actual;

            if (o1.isPresent()) {
                actual = Optional.of(o1.get());
            } else {
                throw supplier.get();
            }
            assertEquals(expected, actual);
        } catch (NullPointerException e) {
            assertEquals(e.getClass(), NullPointerException.class);
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
}

package option;

import org.junit.Test;

import java.io.FileNotFoundException;
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
    public void filter() {
        Optional<String> optional = getOptional();
        Predicate<String> predicate = String::isEmpty;
        Optional<String> actual =
                (optional.isPresent() && predicate.test(optional.get())) ? optional : Optional.empty();
        Optional<String> expected = optional.filter(predicate);

        assertEquals(expected, actual);
    }

    @Test
    public void flatMap() {
        Optional<String> optional = getOptional();
        Function<String, Optional<String>> function = s -> Optional.of(s.toUpperCase());
        Optional<String> actual = (optional.isPresent()) ? function.apply(optional.get()) : Optional.empty();
        Optional<String> expected = optional.flatMap(function);

        assertEquals(expected, actual);
    }

    @Test
    public void orElse() {
        Optional<String> optional = Optional.empty();
        String other = null;
        String actual = optional.isPresent() ? optional.get() : other;
        String expected = optional.orElse(other);

        assertEquals(expected, actual);
    }

    @Test
    public void orElseGet() {
        Optional<String> optional = Optional.empty();
        String toReturn = null;
        Supplier<String> stringSupplier = () -> toReturn;
        String actual = optional.isPresent() ? optional.get() : stringSupplier.get();
        String expected = optional.orElseGet(stringSupplier);

        assertEquals(expected, actual);
    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
                ? Optional.empty()
                : Optional.of("abc");
    }
}

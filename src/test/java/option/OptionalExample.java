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
        final Optional<String> o1 = getOptional();

        final Function<String, Optional<Integer>> getLength = s -> Optional.of(s.length());

        final Optional<Integer> expected = o1.flatMap(getLength);

        final Optional<Integer> actual;
        if (o1.isPresent()) {
            actual = getLength.apply(o1.get());
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void filter() {
        final Optional<String> o1 = getOptional();

        final Predicate<String> getLength = s -> s.length() > 0;

        final Optional<String> expected = o1.filter(getLength);

        final Optional<String> actual;
        if (o1.isPresent()) {
            actual = getLength.test(o1.get()) ? o1 : Optional.empty();
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void orElse() {
        final Optional<String> o1 = getOptional();

        String qqq = "qqq";

        final Optional<String> expected = Optional.of(o1.orElse(qqq));

        final Optional<String> actual;
        if (o1.isPresent()) {
            actual = Optional.of(o1.get());
        } else {
            actual = Optional.of(qqq);
        }

        assertEquals(expected, actual);
    }

    @Test
    public void orElseGet() {
        final Optional<String> o1 = getOptional();

        String qqq = "qqq";
        Supplier<String> stringSupplier = () -> qqq;

        final Optional<String> expected = Optional.of(o1.orElseGet(stringSupplier));

        final Optional<String> actual;
        if (o1.isPresent()) {
            actual = Optional.of(o1.get());
        } else {
            actual = Optional.of(stringSupplier.get());
        }

        assertEquals(expected, actual);
    }

    @Test
    public void orElseThrow() {
        final Optional<String> o1 = getOptional();
        boolean flag = false;
        boolean flag2 = false;

        Supplier<NullPointerException> exceptionSupplier = NullPointerException::new;
        String expected = "";
        String actual = "";

        try {
            expected = o1.orElseThrow(exceptionSupplier);
        } catch (NullPointerException e) {
            flag = true;
        }

        try {
            if (o1.isPresent()) {
                actual = o1.get();
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            flag2 = true;
        }
        assertEquals(flag, flag2);

        if (!flag) {
            assertEquals(expected, actual);
        }
    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
                ? Optional.empty()
                : Optional.of("abc");
    }
}


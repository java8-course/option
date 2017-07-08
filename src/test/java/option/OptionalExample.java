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
    //throw

    @Test(expected = UnsupportedOperationException.class)
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
        final Optional<String> o1 = getOptional();

        final Predicate<String> predicate = s -> false;

        final Optional<String> expected = Optional.empty();

        final Optional<String> actual;
        if (o1.isPresent()) {
            actual = (predicate.test(o1.get())) ? o1 : Optional.empty();
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void flattMap() {
        final Optional<String> o1 = getOptional();

        final Function<String, Optional<Integer>> getLength = e -> Optional.of(e.length());

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
    public void orElse() {
        final Optional<String> o1 = getOptional();

        final String excpected = o1.orElse("else");

        final String actual;

        if (o1.isPresent()) {
            actual = o1.get();
        } else {
            actual = "else";
        }

        assertEquals(excpected, actual);
    }


    @Test
    public void orElseGet() {
        final Optional<String> o1 = getOptional();

        final Supplier<String> supplier = () -> "s";

        final String excpected = o1.orElseGet(supplier);

        final String actual;
        if (o1.isPresent()) {
            actual = o1.get();
        } else {
            actual = supplier.get();
        }
        assertEquals(excpected, actual);
    }

    @Test
    public void optionalThrow() {
        final Optional<String> o1 = getOptional();

        boolean throwed = false;

        final Supplier<RuntimeException> supplier  = RuntimeException::new;
        String                           excpected = "";
        try {
            excpected = o1.orElseThrow(supplier);
        } catch (Exception e) {
            throwed = true;
        }

        String  actual        = "";
        boolean shouldBeThrow = false;
        if (o1.isPresent()) {
            actual = o1.get();
        } else {
            shouldBeThrow = true;
        }

        assertEquals(throwed, shouldBeThrow);
        assertEquals(excpected, actual);
    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
                ? Optional.empty()
                : Optional.of("abc");
    }

    public static <T1, T2, R> Optional<R> zipMap(final Optional<T1> o1, final Optional<T2> o2, final BiFunction<T1, T2, R> f) {
        return o1.flatMap(e -> o2.map(e2 -> f.apply(e, e2)));
    }

    @Test
    public void zipMap() {
        final BiFunction<Integer, Integer, Integer> f = Integer::sum;
        assertEquals(zipMap(Optional.of(1), Optional.of(2), f),  Optional.of(3));
        assertEquals(zipMap(Optional.empty(), Optional.of(2), f), Optional.empty());
        assertEquals(zipMap(Optional.of(1), Optional.empty(), f), Optional.empty());
    }
}

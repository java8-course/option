package option;

import org.junit.Test;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;

public class OptionalExample {

    public static <T1, T2, R> Optional<R> zipMap(Optional<T1> t1, Optional<T2> t2, BiFunction<T1, T2, R> f) {
        if (t1.isPresent() && t2.isPresent()){
            return Optional.of(f.apply(t1.get(), t2.get()));
        } else {
            return Optional.empty();
        }
//        //no mutable
//        //no is present and get
//        //map and flatmap
    }

    @Test
    public void zipMap() {
        Optional<Integer> first = Optional.of(1);
        Optional<String> second = Optional.of("a");

        Optional<String> result = zipMap(first, second, (i, s) -> i + s);
        assertEquals(result.get(), "1a");
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
        final Optional<String> o1 = getOptional();

        final String orElse = "t";

        final String expected = o1.orElse("t");

        final String actual;
        if (o1.isPresent()) {
            actual = o1.get();
        } else {
            actual = orElse;
        }
    }

    @Test
    public void orElseGet() {
        final Optional<String> o1 = getOptional();

        final Supplier<String> getChar = () -> "t";

        final String expected = o1.orElseGet(getChar);

        final String actual;
        if (o1.isPresent()) {
            actual = o1.get();
        } else {
            actual = "t";
        }

        assertEquals(expected, actual);
    }

    @Test
    public void filter() {
        final Optional<String> o1 = getOptional();

        final Predicate<String> isNull = Objects::isNull;

        final Optional<String> expected = o1.filter(isNull);

        final Optional<String> actual;
        if (o1.isPresent() && isNull.test(o1.get())) {
            actual = Optional.of(o1.get());
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void flatMap() {
        final Optional<String> o1 = getOptional();

        final Function<String, Optional<String>> big = s -> Optional.of(s.toUpperCase());

        final Optional<String> expected = o1.flatMap(big);

        final Optional<String> actual;
        if (o1.isPresent()) {
            actual = big.apply(o1.get());
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void orElseThrow() {
        final Optional<String> o1 = getOptional();

        final Supplier<Exception> exception = Exception::new;

        try {
            System.out.println(o1.orElseThrow(exception));
        } catch (Exception e) {
            System.out.println("no value");
        }

        try {
            if (o1.isPresent()) {
                System.out.println(o1.get());
            } else {
                throw exception.get();
            }
        } catch (Exception e) {
            System.out.println("no value");
        }
    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
                ? Optional.empty()
                : Optional.of("abc");
    }
}

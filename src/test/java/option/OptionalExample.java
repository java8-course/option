package option;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class OptionalExample {

    public static <T1, T2, R> Optional<R> zipMap(Optional<T1> o1, Optional<T2> o2, BiFunction<T1, T2, R> f){
        // isPresent, get none
        //use map + flatMap
        Optional<T1> oo1 = o1.flatMap(Optional::ofNullable);
        Optional<T2> oo2 = o2.flatMap(Optional::ofNullable);

//        f.apply(oo1, oo2);
        return null;
    }

    @Test
    public void get() {
        final Optional<String> o1 = Optional.empty();

        o1.ifPresent(s -> assertEquals("abc", s));

        assertEquals("t", o1.orElse("t"));
        assertEquals("t", o1.orElseGet(() -> "t"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void get2() {
        final Optional<String> o1 = Optional.empty();

        o1.orElseThrow(() -> new UnsupportedOperationException());
    }

    @Test
    public void ifPresent() {
        final Optional<String> o1 = getOptional();

        o1.ifPresent(s -> assertEquals("abc", s));

        if (o1.isPresent()) {
            assertEquals("abc", o1.get());
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

        final Optional<Integer> expected = o1.flatMap(s -> Optional.of(s.length()));

        final Optional<Integer> actual;
        if (o1.isPresent()) {
            actual = Optional.of(o1.get().length());
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void filter() {
        final Optional<String> o1 = getOptional();

        o1.filter(s -> s.length() >= 3)
                .ifPresent(s -> assertEquals("abc", s));

        final Optional<String> actual;
        if (o1.isPresent()) {
            actual = Optional.of(o1.get()).filter(s -> s.length() >= 3);
            assertEquals("abc", actual.get());
        } else {
            actual = Optional.empty();
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void filter2() {
        final Optional<String> o1 = getOptional();

        o1.filter(s -> s.length() >= 4)
                .ifPresent(System.out::println);

        final Optional<String> actual;
        if (o1.isPresent()) {
            actual = Optional.of(o1.get()).filter(s -> s.length() >= 4);
            assertEquals("abc", actual.get());
        } else {
            actual = Optional.empty();
            actual.get();
        }
    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
                ? Optional.empty()
                : Optional.of("abc");
    }
}

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

    public static <T1, T2, R> Optional<R> zipMap1(Optional<T1> o1, Optional<T2> o2, BiFunction<T1, T2, R> f) {
        if (o1.isPresent() && o2.isPresent()) {
            return Optional.ofNullable(f.apply(o1.get(), o2.get()));
        } else {
            return Optional.empty();
        }
    }

    public static <T1, T2, R> Optional<R> zipMap2(Optional<T1> o1, Optional<T2> o2, BiFunction<T1, T2, R> f) {
        return o1.flatMap(
                t1 -> o2.flatMap(
                        t2 -> Optional.ofNullable(f.apply(t1, t2))));
    }

    @Test
    public void zipTest() {
        Optional<String> o1 = Optional.of("hello");
        Optional<Integer> o2 = Optional.of(404);

        BiFunction<String, Integer, String> f = (s, i) -> "" + s + i;
        Optional<String> rOption1 = zipMap1(o1, o2, f);
        Optional<String> rOption2 = zipMap2(o1, o2, f);

        assertEquals(rOption1.get(), "hello404");
        assertEquals(rOption2.get(), "hello404");
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
    public void filter() {
        Optional<String> o = getOptional();
        Predicate<String> p = String::isEmpty;
        Optional<String> expected = o.filter(p);

        Optional<String> actual = (o.isPresent() && p.test(o.get()))
                ? o : Optional.empty();

        assertEquals(expected, actual);
    }

    @Test
    public void flatMap() {
        Optional<String> o = getOptional();
        Function<String, Optional<String>> f = (s) -> Optional.of(s + "hello");
        Optional<String> expected = o.flatMap(f);

        Optional<String> actual = (o.isPresent())
                ? f.apply(o.get()) : Optional.empty();

        assertEquals(expected, actual);
    }

    @Test
    public void orElse() {
        Optional<String> o = getOptional();
        String str = "hello";
        String expected = o.orElse(str);

        String actual = (o.isPresent())
                ? o.get() : str;

        assertEquals(expected, actual);
    }

    @Test
    public void orElseGet() {
        Optional<String> o = getOptional();
        Supplier<String> s = () -> "hello";
        String expected = o.orElseGet(s);

        String actual = (o.isPresent())
                ? o.get() : s.get();

        assertEquals(expected, actual);
    }

    @Test
    public void orElseThrow() {
        Optional<String> o = getOptional();
        Supplier<RuntimeException> s = RuntimeException::new;

        String expected = null;
        Exception e1 = new Exception();

        try {
            expected = o.orElseThrow(s);
        } catch (RuntimeException e) {
            e1 = e;
        }

        String actual = null;
        try {
            if (o.isPresent()) {
                actual = o.get();
            } else {
                throw s.get();
            }
        } catch (RuntimeException e) {
            assertEquals(e1.getClass(), e.getClass());
        }

        assertEquals(expected, actual);

    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
            ? Optional.empty()
            : Optional.of("abc");
    }
}

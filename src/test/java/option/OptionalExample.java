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

    //length is min of two collections
    //R only when T1 and T2 exists
    //without isPresent && get
    //without mutable var (only final vars)
    //hint: 2 methods to use: only map and flatMap
    public static <T1, T2, R> Optional<R> zipMap(Optional<T1> o1, Optional<T2> o2, BiFunction<T1, T2, R> bf) {
        return o1.flatMap(t1 -> o2.map(t2 -> bf.apply(t1, t2)));
    }

    public static <T1, T2, R> Optional<R> zipMap1(Optional<T1> o1, Optional<T2> o2, BiFunction<T1, T2, R> bf) {
        Optional<Optional<R>> optionalOptional = o1.map(t1 -> o2.map(t2 -> bf.apply(t1, t2)));
        return optionalOptional.orElse(Optional.empty());
    }

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

    //ifPresent && get -> realise all methods from Optional
    /*
    1. option.filter();      +
    2. option.flatMap();     +
    3. option.orElse();      +
    4. option.orElseGet();   +
    5. option.orElseThrow(); +
    */

    @Test
    public void filter() throws Exception {
        Optional<String> o1 = getOptional();

        Predicate<String> p = s -> s.equals("abc");

        Optional<String> expected = o1.filter(p);
        Optional<String> actual;

        if (o1.isPresent()) {
            if (p.test(o1.get())) {
                actual = o1;
            } else {
                actual = Optional.empty();
            }
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void flatMap() throws Exception {
        Optional<String> o1 = getOptional();

        Function<String, Optional<String>> f = s -> Optional.of("test");

        Optional<String> expected = o1.flatMap(f);
        Optional<String> actual;

        if (o1.isPresent()) {
            actual =  f.apply(o1.get());
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void orElse() throws Exception {
        Optional<String> o1 = getOptional();

        String str = "test";

        String expected = o1.orElse(str);
        String actual;

        if (o1.isPresent()) {
            actual = o1.get();
        } else {
            actual = str;
        }

        assertEquals(expected, actual);
    }

    @Test
    public void orElseGet() throws Exception {
        Optional<String> o1 = getOptional();

        Supplier<String> supplier = () -> "test";

        String expected = o1.orElseGet(supplier);
        String actual;

        if (o1.isPresent()) {
            actual = o1.get();
        } else {
            actual = supplier.get();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void orElseThrow() throws Exception {
        Optional<String> o1 = getOptional();
        Supplier<NullPointerException> thrower = NullPointerException::new;

        boolean thrownedExpected = false;
        String expected = null;
        try {
            expected = o1.orElseThrow(thrower);
        } catch (NullPointerException e) {
            thrownedExpected = true;
        }

        boolean thrownedActual = false;
        String actual = null;
        try {
            if (o1.isPresent()) {
                actual = o1.get();
            } else {
                thrower.get();
            }
        } catch (NullPointerException e) {
            thrownedActual = true;
        }

        assertEquals(thrownedExpected, thrownedActual);
        assertEquals(expected, actual);
    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
            ? Optional.empty()
            : Optional.of("abc");
    }
}

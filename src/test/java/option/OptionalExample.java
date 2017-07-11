package option;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class OptionalExample {

    @Test
    public void get() {
        final Optional<String> o1 = Optional.empty();

        o1.ifPresent(s -> System.out.println(s));

        o1.orElse("t");
        o1.orElseGet(() -> "t");
        assertThrows(UnsupportedOperationException.class,
                () -> o1.orElseThrow(UnsupportedOperationException::new));
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
        actual = o1.map(getLength::apply);

        assertEquals(expected, actual);
    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
            ? Optional.empty()
            : Optional.of("abc");
    }

    @Test
    void filter() {
        Optional<String> o1 = Optional.of("abc");
        Predicate<String> predicate = s -> s.equals("test");
        Optional<String> expected = o1.filter(predicate);
        Optional<String> actual = predicate.test(o1.get())? o1 : Optional.empty();

        assertEquals(expected, actual);


        o1 = Optional.empty();
        expected = o1.filter(predicate);
        actual = Optional.empty();

        assertEquals(expected, actual);
    }

    @Test
    void flatMap() {
        Optional<String> o1 = Optional.of("abc");
        Function<String, Optional<String>> function = s -> Optional.of("test");
        Optional<String> expected = o1.flatMap(function);
        Optional<String> actual;
        actual = function.apply(o1.get());

        assertEquals(expected, actual);

        o1 = Optional.empty();
        expected = o1.flatMap(function);
        actual = Optional.empty();

        assertEquals(expected, actual);
    }

    @Test
    void orElse() {
        Optional<String> o1 = Optional.of("abc");
        String expected = o1.orElse("other");
        String actual;
        actual = o1.get();

        assertEquals(expected, actual);


        o1 = Optional.empty();
        expected = o1.orElse("other");
        actual = "other";

        assertEquals(expected, actual);
    }

    @Test
    void orElseThrow() {
        final Optional<String> o1 = Optional.of("abc");
        Supplier<NullPointerException> supplier = NullPointerException::new;

        final Class<NullPointerException> exception = NullPointerException.class;
        o1.orElseThrow(supplier);
        o1.get();

        final Optional<String> o2 = Optional.empty();
        assertThrows(exception, () -> o2.orElseThrow(supplier));
    }

    @Test
    void orElseGet() {
        Optional<String> o1 = Optional.of("abc");
        final Supplier<String> supplier = () -> "test";
        String expected = o1.orElseGet(supplier);
        String actual = o1.get();

        assertEquals(expected,actual);


        o1 = Optional.empty();
        expected = o1.orElseGet(supplier);
        actual = supplier.get();

        assertEquals(expected,actual);

    }

    // TODO: 07.07.17 fill out and test method using map and flatMap
    private static <T1, T2, R> Optional<R> zipMap(Optional<T1> o1, Optional<T2> o2, BiFunction<T1, T2, R> f) {
        return o1.flatMap(x -> o2.map(y -> f.apply(x,y)));
    }

    @Test
    void testZipMap(){
        final BiFunction<String, String, String> mrTractor = String::concat;

        assertEquals(zipMap(Optional.of("KuKa"), Optional.of("ReKu"), mrTractor),  Optional.of("KuKaReKu"));
        assertEquals(zipMap(Optional.of("Aloha, boys"), Optional.empty(), mrTractor), Optional.empty());
        assertEquals(zipMap(Optional.empty(), Optional.empty(), mrTractor), Optional.empty());
    }
}

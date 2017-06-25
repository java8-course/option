package option;

import com.google.common.base.Strings;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;

public class OptionalExample {

    public static <T1, T2, R> Optional<R> zipMap(Optional<T1> o1, Optional<T2> o2, BiFunction<T1, T2, R> f) {
        throw new UnsupportedOperationException();
    }

    @Test
    public void get() {
        Optional<String> o1 = Optional.empty();

        o1.ifPresent(s -> System.out.println(s));

        o1.orElse("t");
        o1.orElseGet(() -> "t");
        o1.orElseThrow(() -> new UnsupportedOperationException());
    }

    @Test
    public void ifPresent() {
        Optional<String> o1 = this.getOptional();

        o1.ifPresent(System.out::println);

        if (o1.isPresent()) {
            System.out.println(o1.get());
        }
    }

    @Test
    public void map() {
        Optional<String> o1 = this.getOptional();

        Function<String, Integer> getLength = String::length;

        Optional<Integer> expected = o1.map(getLength);

        Optional<Integer> actual;
        if (o1.isPresent()) {
            actual = Optional.of(getLength.apply(o1.get()));
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }


    @Test
    public void filter() {
        Optional<String> o1 = this.getOptional();

        Predicate<String> isEmpty = s -> Strings.isNullOrEmpty(s);

        Optional<String> expected = o1.filter(isEmpty);

        Optional<String> actual;
        if (o1.isPresent()) {
            if (isEmpty.test(o1.get())) {
                actual = Optional.empty();
            } else {
                actual = Optional.of(o1.get());
            }
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

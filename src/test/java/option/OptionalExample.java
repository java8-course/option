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

    //не используя не final переменные
    //не используя isPresent() и get()
    //использовать map() и flatMap()
    public static <T1, T2, R> Optional<R> zipMap(Optional<T1> o1, Optional<T2> o2, BiFunction<T1, T2, R> f){
        throw new UnsupportedOperationException();
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
        final Optional<String> o1 = getOptional();

        final Predicate<String> isEmpty = String::isEmpty;

        final Optional<String> expected = o1.filter(isEmpty);

        final Optional<String> actual;
        if (o1.isPresent()) {
            if(isEmpty.test(o1.get())){
                actual = Optional.empty();
            }
            else{
                actual = Optional.of(o1.get());
            }
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
    public void orElse() {

        final Optional<String> o1 = getOptional();

        final String aNull = "null";
        final Optional<String> expected = Optional.of(o1.orElse(aNull));

        final Optional<String> actual;
        if (o1.isPresent()) {
            actual = Optional.of(o1.get());
        } else {
            actual = Optional.of(aNull);
        }
        assertEquals(expected, actual);
    }
    @Test
    public void orElseGet() {

        final Optional<String> o1 = getOptional();
        final Supplier<String> s = () -> "null";

        final Optional<String> expected = Optional.of(o1.orElseGet(s));

        final Optional<String> actual;
        if (o1.isPresent()) {
            actual = Optional.of(o1.get());
        } else {
            actual = Optional.of(s.get());
        }
        assertEquals(expected, actual);
    }
    @Test
    public void orElseThrow() {

        final Optional<String> o1 = getOptional();
        final String expectedMessage = "Exception was thrown";
        final Supplier<UnsupportedOperationException> ex =() -> new UnsupportedOperationException("Exception was thrown");

        final Optional<String> actual;
        try{
            final Optional<String> expected = Optional.of(o1.orElseThrow(ex));
            if (o1.isPresent()) {
                actual = Optional.of(o1.get());
                assertEquals(expected, actual);
            }
        }
        catch (UnsupportedOperationException e){
            System.out.println(e.getMessage());
            assertEquals(expectedMessage, e.getMessage());
        }

    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
            ? Optional.empty()
            : Optional.of("abc");
    }
}

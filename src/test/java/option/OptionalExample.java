package option;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.assertArrayEquals;
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

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
                ? Optional.empty()
                : Optional.of("abc");
    }

    @Test
    public void orElseGet() throws Exception {

        Optional<String> optional = getOptional();

        String other = "other";

        String expected = optional.orElseGet(() -> other);

        String actual = optional.isPresent() ? optional.get() : other;

        assertEquals(expected, actual);

    }

    @Test
    public void orElse() throws Exception {

        Optional<String> optional = getOptional();

        String other = "other";

        String expected = optional.orElse(other);

        String actual = optional.isPresent() ? optional.get() : other;

        assertEquals(expected, actual);

    }


    @Test
    public void orElseTrow() throws Exception {
        boolean isOptionalTrow = false;
        String extended = "";

        Optional<String> optional = getOptional();
        try {
            extended = optional.orElseThrow(NoSuchElementException::new);
        } catch (NoSuchElementException e) {
            isOptionalTrow = true;
        }

        boolean isImplementationThrow = false;

        String actual = "";
        try {
            if (optional.isPresent()) {
                actual = optional.get();
            } else {
                throw new NoSuchElementException();
            }
        } catch (NoSuchElementException e) {
            isImplementationThrow = true;
        }


        assertEquals(isOptionalTrow, isImplementationThrow);

        if (!isOptionalTrow)
            assertEquals(extended, actual);

    }

    @Test
    public void filter() throws Exception {
        Optional<String> optional = getOptional();

        String s = "abc";
        Optional<String> abc = optional.filter(Predicate.isEqual(s));

        Optional<String> actual = optional.isPresent() ?
                optional.get().equals(s) ?
                        optional :
                        Optional.empty() :
                Optional.empty();

        assertEquals(abc, actual);
    }

    @Test
    public void flatMap() throws Exception {

        Optional<String> optional = getOptional();

        Optional<char[]> expected = optional.flatMap(s1 -> Optional.of(s1.toCharArray()));

        Optional<char[]> actual = optional.isPresent() ?
                Optional.of(optional.get().toCharArray()) :
                Optional.empty();

        if (expected.isPresent()) {
            assertArrayEquals(expected.get(), actual.get());
        } else {
            assertEquals(expected, actual);
        }
    }
}

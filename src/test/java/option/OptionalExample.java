import org.junit.Test;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;

public class OptionalExample {

    @Test
    public void get() {
        final Optional<String> o1 = Optional.empty();

        o1.ifPresent(s -> System.out.println(s));

        o1.orElse("t");
        o1.orElseGet(() -> "t");
        //  o1.orElseThrow(() -> new UnsupportedOperationException());
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
        final Function<String, Optional<Integer>> getLengthOpt = s -> Optional.of(s.length());
        final Optional<Integer> expected = o1.flatMap(getLengthOpt);
        final Optional<Integer> actual;

        if (o1.isPresent()) {
            actual = Optional.of(getLengthOpt.apply(o1.get()).get());
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void filter() {
        final Optional<String> o1 = getOptional();
        Predicate<String> predicate = p -> p.startsWith("a");
        final Optional<String> expected = o1.filter(predicate);
        final Optional<String> actual;

        if (o1.isPresent()) {
            actual = predicate.test(o1.get()) ? Optional.of(o1.get()) : Optional.empty();
        } else {
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void orElse() {
        final Optional<String> o1 = getOptional();
        String expected = o1.orElse("W");
        String actual;

        if (o1.isPresent()) {
            actual = o1.get();
        } else {
            actual = "W";
        }
        assertEquals(expected, actual);
    }

    @Test
    public void orElseGet() {
        final Optional<String> o1 = getOptional();
        String expected = o1.orElseGet(() -> "what");
        String actual;

        if (o1.isPresent()) {
            actual = o1.get();
        } else {
            actual = "what";
        }
        assertEquals(expected, actual);
    }

    @Test
    public void orElseThrow() {
        final Optional<String> o1 = getOptional();
        String expected = null;
        try {
            expected = o1.orElseThrow(() -> new Exception("what"));
        } catch (Exception e) {
            assertEquals(e.getMessage(), "what");
            return;
        }
        String actual;
        actual = o1.get();

        assertEquals(expected, actual);
    }

    @Test
    public void toStringOpt() {
        final Optional<String> o1 = getOptional();

        String expected = o1.toString();
        String actual;
        if (o1.isPresent()) {
            actual = "Optional[" + o1.get() + "]";
        } else {
            actual = "Optional.empty";
        }

        assertEquals(expected, actual);
    }

    @Test
    public void hashcode() {
        final Optional<String> o1 = getOptional();

        Integer expected = o1.hashCode();
        Integer actual;
        if (o1.isPresent()) {
            actual = o1.get().hashCode();
        } else {
            actual = 0;
        }

        assertEquals(expected, actual);
    }

    @Test
    public void equalsOpt() {
        final Optional<String> o1 = getOptional();
        Optional<String> o2 = Optional.of("abc");
        boolean expected = o1.equals(o2);
        boolean actual;
        if (o1.isPresent()) {
            actual = o1.get().equals(o2.get());
        } else {
            actual = !o2.isPresent();
        }
        assertEquals(expected, actual);
    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean() ? Optional.empty() : Optional.of("abc");
    }
}

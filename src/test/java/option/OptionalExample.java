package option;

import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.Assert.*;

public class OptionalExample {

    public static <T1, T2, R> Optional<R> zipMap(Optional<T1> o1, Optional<T2> o2, BiFunction<T1,T2, R> function) {
        return o1.flatMap(t1 -> o2.map(t2 -> function.apply(t1,t2)));
    }

    @Test
    public void zipTest() {
        final Optional<String> s1 = Optional.of("123");
        final Optional<String> s2 = Optional.of("456");

        assertEquals(zipMap(s1,s2, (t1,t2) -> t1 + t2), Optional.of("123456"));
    }

    @Test
    public void zipTest2() {
        final Optional<String> s1 = Optional.empty();
        final Optional<String> s2 = Optional.empty();

        assertEquals(zipMap(s1,s2, (t1,t2) -> t1 + t2), Optional.empty());
    }

    @Test
    public void zipTest3() {
        final Optional<String> s1 = Optional.of("123");
        final Optional<String> s2 = Optional.empty();

        assertEquals(zipMap(s1,s2, (t1,t2) -> t1 + t2), Optional.empty());
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
        final Optional<String> optional = getOptional();

        final Function<String, Optional<Integer>> function = s ->
                s.length() > 0 ? Optional.of(s.length()) : Optional.empty();

        final Optional<Integer> actual = optional.flatMap(function);

        if (optional.isPresent()) {
            assertEquals(actual, Optional.of(optional.get().length()));
        } else {
            assertEquals(actual, Optional.empty());
        }
    }

    @Test
    public void orElse() {
        final Optional<String> optional = getOptional();

        final String actual = optional.orElse("EMPTY");

        if (optional.isPresent()) {
            assertEquals(actual, optional.get());
        } else {
            assertEquals(actual, "EMPTY");
        }
    }

    @Test
    public void orElseThrow() {
        final Optional<String> optional = getOptional();

        if (optional.isPresent()) {
            final String actual = optional.orElseThrow(RuntimeException::new);
            assertEquals(actual, optional.get());
        } else {
            boolean flag = false;
            try {
                optional.orElseThrow(RuntimeException::new);
            } catch (RuntimeException ex) {
                flag = true;
            }
            assertTrue(flag);
            //unreachable
        }
    }

    @Test
    public void orElseGet() {
        final Optional<String> optional = getOptional();

        final String actual = optional.orElseGet(String::new);

        if (optional.isPresent()) {
            assertEquals(actual, optional.get());
        } else {
            assertEquals(actual, "");
        }
    }

    @Test
    public void filter() {
        final Optional<String> optional = getOptional();

        final Optional<String> actual = optional.filter(s -> s.length() == 3);

        if (optional.isPresent()) {
            assertTrue(actual.isPresent());
        } else {
            assertFalse(actual.isPresent());
        }
    }

    @Test
    public void ifPresent() {
        final Optional<String> optional = getOptional();


        boolean[] mas = new boolean[]{false};
        optional.ifPresent(s -> mas[0] = true);

        if (optional.isPresent()) {
            assertTrue(mas[0]);
        } else {
            assertFalse(mas[0]);
        }
    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
            ? Optional.empty()
            : Optional.of("abc");
    }
}

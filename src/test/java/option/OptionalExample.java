package option;

import com.sun.istack.internal.Nullable;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class OptionalExample {

    private final String TEST_VALUE_1 = "TEST_VALUE_1";
    private final String TEST_VALUE_2 = "TEST_VALUE_2";

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

    @Test(expected = NoSuchElementException.class)
    public void orELseThrow1() {
        Optional<String> optional = Optional.empty();
        optional.orElseThrow(() -> new NoSuchElementException("empty value"));
    }

    @Test
    public void orELseThrow2() {
        Optional<String> optional = Optional.of(TEST_VALUE_1);
        String fromOptional = optional.orElseThrow(() -> new NoSuchElementException("empty value"));
        assertThat(fromOptional, is(TEST_VALUE_1));
    }

    @Test
    public void flatMap() {
        class TesterFlatMap {
            @Nullable
            private String field;

            Optional<String> getField() {
                return Optional.ofNullable(field);
            }

            void setField(String field) {
                this.field = field;
            }
        }
        TesterFlatMap testerFlatMap = new TesterFlatMap();
        testerFlatMap.setField(TEST_VALUE_1);
        assertTrue(Optional.of(testerFlatMap)
                .flatMap(TesterFlatMap::getField)
                .isPresent());
        testerFlatMap.setField(null);
        assertFalse(Optional.of(testerFlatMap)
                .flatMap(TesterFlatMap::getField)
                .isPresent());
    }

    @Test
    public void testOrElseGet() {
        String actual = (String) Optional.empty().orElseGet(this::getTestValue);
        String expected = getTestValue();
        assertThat(actual, is(expected));
    }

    public String getTestValue() {
        return "test";
    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
                ? Optional.empty()
                : Optional.of("abc");
    }

    public static <T1, T2, R> Optional<R> zipMap(Optional<T1> o1, Optional<T2> o2, BiFunction<T1, T2, R> f) {
        return o1.flatMap(obj1 -> o2.map(obj2 -> f.apply(obj1, obj2)));
    }

    @Test
    public void filter() {
        Optional<String> optional_1 = Optional.of(TEST_VALUE_1);
        optional_1 = optional_1.filter(s -> s.startsWith("J"));
        assertFalse(optional_1.isPresent());

        Optional<String> optional_2 = Optional.of(TEST_VALUE_2);
        optional_2 = optional_2.filter(s -> s.startsWith("T"));
        assertTrue(optional_2.isPresent());
    }

    @Test
    public void testZipMap() {
        BiFunction<String, String, String> function = String::concat;
        final Optional<String> hello = Optional.of("Hello");
        final Optional<String> world = Optional.of(" world");
        assertThat(zipMap(hello, world, function), is(Optional.of("Hello world")));
        assertThat(zipMap(hello, Optional.empty(), function), is(Optional.empty()));
        assertThat(zipMap(Optional.empty(), hello, function), is(Optional.empty()));
        assertThat(zipMap(Optional.empty(), Optional.empty(), function), is(Optional.empty()));
    }
}
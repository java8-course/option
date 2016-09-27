package option;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class OptionalExample {
    Optional<String> o1;

    @Before
    public void setup() {
        o1 = getOptional();
    }

    @Test
    public void get() {
        o1.ifPresent(System.out::println);

        o1.orElse("t");
        o1.orElseGet(() -> "t");
        //o1.orElseThrow(UnsupportedOperationException::new);
    }

    @Test
    public void ifPresent() { // Через get() и isPresent() реализовать все методы Optional
        o1.ifPresent(System.out::println);

        if (o1.isPresent()) {
            System.out.println(o1.get());
        }
    }

    @Test
    public void filter() {
        final Optional<String> o1Filtered = o1.isPresent() && o1.get().equals("abc") ?
                o1 : Optional.empty();

        assertThat(o1Filtered, is(o1.filter("abc"::equals)));
    }

    @Test
    public void map() {
        final Optional<String> o1Mapped = o1.isPresent() ? Optional.of(o1.get() + "d") : Optional.empty();

        assertThat(o1Mapped, is(o1.map(s -> s + "d")));
    }

    @Test
    public void orElse() {
        final String s = o1.isPresent() ? o1.get() : "d";

        assertThat(s, is(o1.orElse("d")));
    }

    @Test
    public void orElseGet() {
        final String s = o1.isPresent() ? o1.get() : ((Supplier<String>) () -> "d").get();

        assertThat(s, is(o1.orElseGet(() -> "d")));
    }

    @Test
    public void orElseThrow() {
        try {
            final String s;
            if (o1.isPresent()) s = o1.get();
            else throw new IllegalArgumentException();
            assertThat(s, is(o1.get()));
        } catch (IllegalArgumentException e) {
            assertFalse(o1.isPresent());
        }
    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
                ? Optional.empty()
                : Optional.of("abc");
    }
}

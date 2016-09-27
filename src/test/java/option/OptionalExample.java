package option;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import static org.hamcrest.core.Is.is;

public class OptionalExample {

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
                ? Optional.empty()
                : Optional.of("abc");
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
    public void filter() {
        final Optional<String> o = getOptional();
        final Optional<String> oFilter =
                o.isPresent() && o.get().equals("abc")
                        ? o
                        : Optional.empty();

        Assert.assertThat(oFilter, is(o.filter("abc"::equals)));
    }

    @Test
    public void map() {
        final Optional<String> o = getOptional();
        final Optional<String> oMap =
                o.isPresent()
                        ? Optional.of(o.get() + o.get())
                        : Optional.empty();

        Assert.assertThat(oMap, is(o.map(s -> s.concat(s))));
    }

    @Test
    public void orElse() {
        final Optional<String> o = getOptional();
        final String s = o.isPresent()
                ? o.get()
                : "123";

        Assert.assertThat(s, is(o.orElse("123")));
    }

    @Test
    public void orElseGet() {
        final Optional<String> o = getOptional();
        final String s = o.isPresent()
                ? o.get()
                : ((Supplier<String>) () -> "123").get();

        Assert.assertThat(s, is(o.orElseGet(() -> "123")));
    }

    @Test
    public void orElseThrow() {
        // TODO ?
    }






}

package option;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class OptionalExample {
    @Test(expected = UnsupportedOperationException.class)
    public void get() {
        final Optional<String> o1 = Optional.empty();

        o1.ifPresent(System.out::println);

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
    public void filter(){
        final Optional<String> o1 = getOptional();

        final Optional<String> expected = o1.filter((s) -> s.length() < 5);

        final Optional<String> actual;

        if (o1.isPresent()){
            if (o1.get().length() < 5)
                actual = Optional.of(o1.get());
            else actual = Optional.empty();
        } else actual = Optional.empty();

        assertThat(expected, equalTo(actual));
    }

    @Test
    public void flatMap() {
        final Optional<String> o1 = getOptional();

        final Function<String, Optional<String>> f = Optional::of;

        final Optional<String> expected = o1.flatMap(f);

        final Optional<String> actual;

        if (o1.isPresent()){
            actual = f.apply(o1.get());
        } else actual = Optional.empty();

        assertThat(expected, equalTo(actual));

    }

    @Test
    public void orElse() {
        final Optional<String> o1 = getOptional();

        final String els = "cba";

        final String expected = o1.orElse(els);

        final String actual;

        if (o1.isPresent()){
            actual = o1.get();
        } else actual = els;

        assertThat(expected, equalTo(actual));

    }

    @Test
    public void orElseGet() {
        final Optional<String> o1 = getOptional();

        final Supplier<String> s = () -> "cba";

        final String expected = o1.orElseGet(s);

        final String actual;

        if (o1.isPresent()){
            actual = o1.get();
        } else actual = s.get();

        assertThat(expected, equalTo(actual));

    }

    @Test(expected = NoSuchElementException.class)
    public void orElseThrow() {
        final Optional<String> o1 = getOptional();

        final Supplier<NoSuchElementException> s = NoSuchElementException::new;

        final String actual;

        if (o1.isPresent()){
            actual = o1.get();
        } else throw s.get();

        final String expected = o1.orElseThrow(s);

        assertThat(expected, equalTo(actual));

        throw new NoSuchElementException();
    }


}

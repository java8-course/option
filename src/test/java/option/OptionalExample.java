package option;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class OptionalExample {


    @Test
    public void get() {
        final Optional<String> o1 = Optional.empty();

        o1.ifPresent(System.out::println);

        o1.orElse("t");
        o1.orElseGet(() -> "t");
//        o1.orElseThrow(UnsupportedOperationException::new);
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
    public void filter(){
        final Optional<String> o1 = getOptional();

        final Predicate<String> predicate = s -> s.equals("abc");
        final Optional<String> expected = o1.filter(predicate);
        final Optional<String> actual;

        if(o1.isPresent()){
            actual = predicate.test(o1.get()) ? o1 : Optional.empty();
        }
        else {
            actual = Optional.empty();
        }

        assertThat(expected,is(actual));
    }

    @Test
    public void flatMap(){
        final Optional<String> o1 = getOptional();

        final Optional<Integer> optional = o1.flatMap(s -> Optional.of(s.length()));

        final Function<String, Optional<Integer>> function = s -> Optional.of(s.length());

        final Optional<Integer> actual;

        if(o1.isPresent()){
            actual = function.apply(o1.get());
        }
        else {
            actual = Optional.empty();
        }

        assertThat(optional,is(actual));
    }

    @Test
    public void orElse(){
        final Optional<String> o1 = getOptional();

        final String actual = o1.orElse("defaultValue");

        final String expected = o1.isPresent() ? o1.get() : "defaultValue";

        assertThat(actual, is(expected));
    }

    @Test
    public void orElseGet(){
        final Optional<String> o1 = getOptional();

        final Supplier<String> supplier = () -> "something";

        final String actual = o1.orElseGet(() -> "something");

        final String expected = o1.isPresent() ? o1.get() : supplier.get();

        assertThat(actual,is(expected));
    }

    @Test
    public void orElseThrow(){
        final Optional<String> o1 = getOptional();

        final String actual = o1.orElseThrow(() -> new RuntimeException("Error"));

        Supplier<RuntimeException> exceptionSupplier =() -> new RuntimeException("Error");

        final String expected;
        if(o1.isPresent()){
            expected = o1.get();
        }
        else {
            throw exceptionSupplier.get();
        }

        assertThat(actual, is(expected));
    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
            ? Optional.empty()
            : Optional.of("abc");
    }
}

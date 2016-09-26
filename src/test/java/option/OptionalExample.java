package option;

import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.Assert.*;

public class OptionalExample {

    @Test
    public void get() {
        final Optional<String> o1 = Optional.empty();

        o1.ifPresent(System.out::println);

        o1.orElse("t");
        o1.orElseGet(() -> "t");
        o1.orElseThrow(UnsupportedOperationException::new);
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
        final Optional<String> o1 = getOptional();

        Optional<String> s1 = o1.filter(s -> s.length() > 1);

        Predicate<String> p = s -> s.length() > 1;
        Optional<String> s2 = !o1.isPresent() ? Optional.empty() :
                (p.test(o1.get()) ? Optional.of(o1.get()) : Optional.empty());

        assertEquals(s1, s2);
    }

    @Test
    public void flatMap() {
        final Optional<String> o1 = getOptional();

        Optional<Integer> s1 = o1.flatMap(s -> Optional.of(s.length()));

        Function<String, Optional<Integer>> f = s -> Optional.of(s.length());
        Optional<Integer> s2 = o1.isPresent() ? f.apply(o1.get()) : Optional.empty();

        assertEquals(s1, s2);
    }

    @Test
    public void map() {
        final Optional<String> o1 = getOptional();

        Optional<String> s1 = o1.map(s -> "And we have " + s);

        Function<String, String> f = s -> "And we have " + s;
        Optional<String> s2 = o1.isPresent() ? Optional.of(f.apply(o1.get())) : Optional.empty();

        assertEquals(s1, s2);
    }

    @Test
    public void orElse() {
        final Optional<String> o1 = getOptional();

        String s1 = o1.orElse("Boom!");

        String s2 = o1.isPresent() ? o1.get() : "Boom!";

        assertEquals(s1, s2);
    }

    @Test
    public void orElseGet() {
        final Optional<String> o1 = getOptional();

        String s1 = o1.orElseGet(() -> "Some");

        Supplier<String> supplier = () -> "Some";
        String s2 = o1.isPresent() ? o1.get() : supplier.get();

        assertEquals(s1, s2);
    }

    @Test
    public void orElseThrow() {
        final Optional<String> o1 = getOptional();

        String s1 = o1.orElseThrow(() -> new RuntimeException("Error!"));

        Supplier<RuntimeException> supplier = () -> new RuntimeException("Error!");
        String s2;
        if (o1.isPresent()) s2 = o1.get();
        else throw supplier.get();

        assertEquals(s1, s2);
    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
                ? Optional.empty()
                : Optional.of("abc");
    }
}

package option;

import org.junit.Test;

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
    public void flatMap(){
        final Optional<String> o1 = getOptional();
        final Function<String, Optional<String>> substring = (String s) -> Optional.of(s.substring(1));
        final Optional<String> expected = o1.flatMap(substring);
        final Optional<String> actual;

        if(o1.isPresent()){
            actual = substring.apply(o1.get());
        }else{
            actual = Optional.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void filter(){
        final Optional<String> o1 = getOptional();
        final Predicate<String> isEmpty = (String s) -> s.isEmpty();
        final Optional<String> expected = o1.filter(isEmpty);
        final Optional<String> actual;

        if(o1.isPresent()){
            if(isEmpty.test(o1.get())){
                actual = o1;
            }else{
                actual = o1.empty();
            }
        }else{
            actual = o1.empty();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void orElse(){
        final Optional<String> o1 = getOptional();
        final String defValue = "default value";
        final String expected = o1.orElse(defValue);
        final String actual;

        if(o1.isPresent()){
            actual = o1.get();
        }else{
            actual = defValue;
        }

        assertEquals(expected, actual);
    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
            ? Optional.empty()
            : Optional.of("abc");
    }
}

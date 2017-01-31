package option;

import org.junit.Assert;
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
        //o1.orElseThrow(UnsupportedOperationException::new);
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
        final Predicate<String> pred = "abc"::equals;
        final Optional<String> expected = o1.filter(pred);
        final Optional<String> actual;
        if (o1.isPresent()){
            actual = pred.test(o1.get()) ? o1 : Optional.empty();
        } else{
            actual = Optional.empty();
        }
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void flatMap(){
        final Optional<String> o1 = getOptional();
        final Function<String, Optional<String>> flatMapFunction = s -> Optional.of(String.valueOf(s.length()));
        final Optional<String> expected = o1.flatMap(flatMapFunction);
        final Optional<String> actual;
        if (o1.isPresent()){
            actual = flatMapFunction.apply(o1.get());
        } else{
            actual = Optional.empty();
        }
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void orElse(){
        final Optional<String> o1 = getOptional();
        final String expected = o1.orElse("def");
        final String actual;
        if (o1.isPresent()){
            actual = o1.get();
        } else{
            actual = "def";
        }
        Assert.assertEquals(expected,actual);
    }

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
            ? Optional.empty()
            : Optional.of("abc");
    }
}

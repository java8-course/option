package option;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
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

    private Optional<String> getOptional() {
        return ThreadLocalRandom.current().nextBoolean()
            ? Optional.empty()
            : Optional.of("abc");
    }

    @Test
    public void filter(){
        final Optional<String> o1 = getOptional();
        final Predicate<String> predicate = s -> s.equals("test");
        Optional<String> expected = o1.filter(predicate);
        Optional<String> actual;
        if(o1.isPresent()){
            if(predicate.test(o1.get())){
                actual = o1;
            }else{
                actual = o1.empty();
            }
        }else{
            actual = o1.empty();
        }

        assertEquals(expected,actual);

    }

    @Test
    public void flatMap(){
        final Optional<String> o1 = getOptional();

    }
    //TODO method zip (слияние двух коллекций) - zipmap
    //map and flatmap (не использовать isPresent, get) - соединяем два optional
    public static <T1,T2,R> Optional<R> zipMap(Optional<T1> o1, Optional<T2> o2, BiFunction<T1,T2,R> f){

    }
}

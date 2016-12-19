package option;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

// https://github.com/java8-course/option
public abstract class Option<T> {

    private static final None EMPTY = new None();

    public static <T> Option<T> empty() {
        return EMPTY;
    }

    public static <T> Option<T> of(T t) {
        return new Some<T>(t);
    }

    public static <T> Option<T> ofNullable(T t) {
        return null == t ? empty() : of(t);
    }

    private Option() {}

    private static class None<T> extends Option<T> {

        @Override
        public Option<T> filter(Predicate<T> p) {
            return empty();
        }

        @Override
        public <R> Option<R> map(Function<T, R> f) {
            return empty();
        }

        @Override
        public <R> Option<R> flatMap(Function<T, Option<R>> f) {
            return empty();
        }

        @Override
        public void forEach(Consumer<T> c) {
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public T orElse(T t) {
            return t;
        }
    }

    private static class Some<T> extends Option<T> {

        private final T value;

        public Some(T value) {
            this.value = value;
        }

        @Override
        public Option<T> filter(Predicate<T> p) {
            return p.test(value) ? this : empty();
        }

        @Override
        public <R> Option<R> map(Function<T, R> f) {
            return of(f.apply(value));
        }

        @Override
        public <R> Option<R> flatMap(Function<T, Option<R>> f) {
            return f.apply(value);
        }

        @Override
        public void forEach(Consumer<T> c) {
            c.accept(value);
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public T orElse(T t) {
            return value;
        }
    }


//    filter
    public abstract Option<T> filter(Predicate<T> p);
//    map
    public abstract <R> Option<R> map(Function<T, R> f);
//    flatMap
    public abstract <R> Option<R> flatMap(Function<T, Option<R>> f);
//    forEach
    public abstract void forEach(Consumer<T> c);
//    isEmpty
    public abstract boolean isEmpty();
//    orElse
    public abstract T orElse(T t);
}










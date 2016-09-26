package option;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

// https://github.com/java8-course/option
public abstract class Option<T> {
    static <T> Option<T> empty() {
        return new None<T>();
    }

    static <T> Option<T> of(T t) {
        return new Some<T>(t);
    }

    static <T> Option<T> ofNullable(T t) {
        return t == null ? empty() : of(t);
    }

    private Option() {}

    public static class Some<T> extends Option<T> {
        private final T t;

        public Some(T t) {
            this.t = Objects.requireNonNull(t);
        }

        @Override
        public Option<T> filter(Predicate<T> p) {
            return p.test(t) ? this : empty();
        }

        @Override
        public <R> Option<R> map(Function<T, R> f) {
            return of(f.apply(t));
        }

        @Override
        public <R> Option<R> flatMap(Function<T, Option<R>> f) {
            return f.apply(t);
        }

        @Override
        public void forEach(Consumer<T> c) {
            c.accept(t);
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Option<T> orElse(Option<T> o2) {
            return this;
        }

        @Override
        public T getOrElse(Supplier<T> t) {
            return this.t;
        }
    }

    public static class None<T> extends Option<T> {
        public None() {}

        @Override
        public Option<T> filter(Predicate<T> p) {
            return this;
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
        public Option<T> orElse(Option<T> o2) {
            return o2;
        }

        @Override
        public T getOrElse(Supplier<T> t) {
            return t.get();
        }
    }


    public abstract Option<T> filter(Predicate<T> p);
    public abstract <R> Option<R> map(Function<T, R> f);
    public abstract <R> Option<R> flatMap(Function<T, Option<R>> f);
    public abstract void forEach(Consumer<T> c);
    public abstract boolean isEmpty();
    public abstract Option<T> orElse(Option<T> o2);
    public abstract T getOrElse(Supplier<T> t);

}










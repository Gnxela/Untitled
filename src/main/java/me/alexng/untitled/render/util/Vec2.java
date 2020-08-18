package me.alexng.untitled.render.util;

public interface Vec2<T> {

    T getX();

    T getY();

    Vec2<T> add(T x, T y);

    Vec2<T> sub(T x, T y);

    Vec2<T> mul(T x, T y);

    T dot(T x, T y);

    Vec2<T> div(T x, T y);

    Double distance(T x, T y);

    T length();

    default Vec2<T> add(Vec2<T> v) {
        return add(v.getX(), v.getY());
    }

    default Vec2<T> sub(Vec2<T> v) {
        return sub(v.getX(), v.getY());
    }

    default Vec2<T> mul(Vec2<T> v) {
        return mul(v.getX(), v.getY());
    }

    default T dot(Vec2<T> v) {
        return dot(v.getX(), v.getY());
    }

    default Vec2<T> div(Vec2<T> v) {
        return div(v.getX(), v.getY());
    }

    default Double distance(Vec2<T> v) {
        return distance(v.getX(), v.getY());
    }
}

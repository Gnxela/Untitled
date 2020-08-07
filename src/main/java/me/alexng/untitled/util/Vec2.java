package me.alexng.untitled.util;

public interface Vec2<T> {

    Vec2d add(T x, T y);
    Vec2d sub(T x, T y);
    Vec2d mul(T x, T y);
    Vec2d div(T x, T y);
    Double distance(T x, T y);
    T length();
}

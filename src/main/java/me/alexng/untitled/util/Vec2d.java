package me.alexng.untitled.util;

public class Vec2d implements Vec2<Double> {

    // TODO: All these operations create new objects. May be inefficient

    private double x, y;

    public Vec2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Vec2d add(Double x, Double y) {
        return new Vec2d(this.x + x, this.y + y);
    }

    @Override
    public Vec2d sub(Double x, Double y) {
        return new Vec2d(this.x - x, this.y - y);
    }

    @Override
    public Vec2d mul(Double x, Double y) {
        return new Vec2d(this.x * x, this.y * y);
    }

    @Override
    public Vec2d div(Double x, Double y) {
        return new Vec2d(this.x / x, this.y / y);
    }

    @Override
    public Double distance(Double x, Double y) {
        return Math.sqrt((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y));
    }

    @Override
    public Double length() {
        return Math.sqrt(x * x + y * y);
    }
}

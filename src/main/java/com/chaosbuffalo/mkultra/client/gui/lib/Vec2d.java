package com.chaosbuffalo.mkultra.client.gui.lib;

public class Vec2d {
    public int x;
    public int y;

    public Vec2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Vec2d subtract(Vec2d other) {
        return this.subtract(other.x, other.y);
    }

    public Vec2d subtract(int xVal, int yVal) {
        return this.add(-xVal, -yVal);
    }

    public Vec2d add(Vec2d other) {
        return this.add(other.x, other.y);
    }

    public Vec2d add(int xVal, int yVal) {
        return new Vec2d(x + xVal, y + yVal);
    }
}

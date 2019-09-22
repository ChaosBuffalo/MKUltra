package com.chaosbuffalo.mkultra.client.gui.lib;

public class Vec2i {
    public int x;
    public int y;

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2i subtract(Vec2i other) {
        return this.subtract(other.x, other.y);
    }

    public Vec2i subtract(int xVal, int yVal) {
        return this.add(-xVal, -yVal);
    }

    public Vec2i add(Vec2i other) {
        return this.add(other.x, other.y);
    }

    public Vec2i add(int xVal, int yVal) {
        return new Vec2i(x + xVal, y + yVal);
    }
}

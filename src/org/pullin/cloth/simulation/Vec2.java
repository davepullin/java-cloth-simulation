/*
 * COLDLOGIC CONFIDENTIAL UNTIL DETERMINED OTHERWISE
 */
package org.pullin.cloth.simulation;

/**
 *
 * @author root
 */
public class Vec2 {

    public float x = 0;
    public float y = 0;

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2() {
        this(0, 0);
    }

    public Vec2(Vec2 xy) {
        this(xy.x, xy.y);
    }

    public void set(Vec2 xy) {
        this.x = xy.x;
        this.y = xy.y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }
}

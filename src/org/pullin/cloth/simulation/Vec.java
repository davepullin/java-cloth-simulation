/*
 * COLDLOGIC CONFIDENTIAL UNTIL DETERMINED OTHERWISE
 */
package org.pullin.cloth.simulation;

import java.awt.geom.Path2D;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 *
 * @author root
 */
public class Vec {

    private float x = 0;
    private float y = 0;

    public Vec(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec() {
        this(0, 0);
    }

    public Vec(Vec xy) {
        this(xy.x, xy.y);
    }

    public void set(Vec xy) {
        this.x = xy.x;
        this.y = xy.y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(TriFunction<Float,Float, Float, Float> op, Vec vec, Vec vec2) {
        this.x = op.apply(this.x, vec.x, vec2.x);
        this.y = op.apply(this.y, vec.y, vec2.y);

    }
    public void set(BiFunction<Float, Float, Float> op, Vec vec) {
        this.x = op.apply(this.x, vec.x);
        this.y = op.apply(this.y, vec.y);

    }
    public void set(Function<Float, Float> op) {
        this.x = op.apply(this.x);
        this.y = op.apply(this.y);

    }

    public Vec diff(Vec pos) {
        return new Vec(this.x - pos.x,
                this.y - pos.y);
    }

    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }

    void moveTo(Path2D path2d) {
        path2d.moveTo(x, y);
    }

    void lineTo(Path2D path2d) {
        path2d.lineTo(x, y);
    }

    void keep_inside(int boundsx, int boundsy) {
        if (this.x > boundsx) {
            this.x = 2 * boundsx - this.x;
        } else {
            if (1 > this.x) {
                this.x = 2 - this.x;
            }
        }
        
        if (this.y < 1) {
            this.y = 2 - this.y;
        } else {
            if (this.y > boundsy) {
                this.y = 2 * boundsy - this.y;
            }
        }
    }
    
    public void check(float x_c, float y_c) {
        if (this.x != x_c || this.y != y_c) {
            System.out.println("get failure: " + this.x + "," + this.y + "!=" + x_c + "," + y_c);
        }
    }

    public abstract interface TriFunction<T0, T1, T2, T3> {

        abstract T3 apply(T0 t0,T1 t1,T2 t2);
    }
}

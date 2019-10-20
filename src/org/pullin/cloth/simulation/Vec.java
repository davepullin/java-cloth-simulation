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
    private float z = 0;

    public Vec(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec() {
        this(0, 0, 0.0F);
    }

    public Vec(Vec xy) {
        this(xy.x, xy.y, xy.z);
    }

    public void set(Vec xy) {
        this.x = xy.x;
        this.y = xy.y;
        this.z = xy.z;
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(TriFunction<Float, Float, Float, Float> op, Vec vec, Vec vec2) {
        this.x = op.apply(this.x, vec.x, vec2.x);
        this.y = op.apply(this.y, vec.y, vec2.y);
        this.z = op.apply(this.z, vec.z, vec2.z);

    }

    public void set(BiFunction<Float, Float, Float> op, Vec vec) {
        this.x = op.apply(this.x, vec.x);
        this.y = op.apply(this.y, vec.y);
        this.z = op.apply(this.z, vec.z);

    }

    public void set(Function<Float, Float> op) {
        this.x = op.apply(this.x);
        this.y = op.apply(this.y);
        this.z = op.apply(this.z);

    }

    public Vec diff(Vec pos) {
        return new Vec(this.x - pos.x, this.y - pos.y, this.z - pos.z);
    }

    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    void moveTo(Path2D path2d) {
        transform();
        path2d.moveTo(x, y);
    }

    void lineTo(Path2D path2d) {
        transform();
        path2d.lineTo(x, y);
    }

    void keep_inside(Vec bounds) {
        this.set((p, b) -> {
            if (p < 1) {
                p = 2 - p;
            } else if (p > b) {
                p = 2 * b - p;
            }
            return p;
        }, bounds);

//        if (this.x < 1) {
//            this.x = 2 - this.x;
//        } else if (this.x > boundsx) {
//            this.x = 2 * boundsx - this.x;
//        }
//
//        if (this.y < 1) {
//            this.y = 2 - this.y;
//        } else {
//            if (this.y > boundsy) {
//                this.y = 2 * boundsy - this.y;
//            }
//        }
    }

    public void check(float x_c, float y_c) {
        if (this.x != x_c || this.y != y_c) {
            System.out.println("get failure: " + this.x + "," + this.y + "!=" + x_c + "," + y_c);
        }
    }

    private void transform() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public abstract interface TriFunction<T0, T1, T2, T3> {

        abstract T3 apply(T0 t0, T1 t1, T2 t2);
    }
}

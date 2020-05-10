/*
 * Copyright Dave Pullin. Licensed proprietary property. see http://davepullin.com/license
 */
package org.pullin.cloth.simulation;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import static org.pullin.cloth.simulation.Main.debug_projection_transform;
import static org.pullin.cloth.simulation.Main.projection_off;

/**
 *
 * Copyright Dave Pullin. Licensed proprietary property. see
 * http://davepullin.com/license
 */
public class Vec {

    private float x = 0;
    private float y = 0;
    private float z = 0;
    private float marker_size = 5;
    private boolean mark = true;

    public Vec(float x, float y, float z) {
        set(x, y, z);
    }

    public Vec(Integer... i) {
        this((float) i[0], (float) i[1], (float) i[2]);
    }

    public Vec() {
        this(0, 0, 0.0F);
    }
    
    public Vec(Vec vec1, Vec vec2, BiFunction<Float, Float, Float> op){
        this(vec1);
        set(op,vec2);
    }

    public Vec(Vec xy) {
        this(xy.x, xy.y, xy.z);
    }

    public void set(Vec xy) {
        set(xy.x, xy.y, xy.z);
    }

    public final void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        isNan();
    }

    private boolean isNan() {
        if (Float.isNaN(x) || Float.isNaN(y) || Float.isNaN(z)) {
            throw new RuntimeException /*System.out.println*/("Point is Nan: " + this.toString());
        }
        return false;
    }

    public void set(TriFunction<Float, Float, Float, Float> op, Vec vec, Vec vec2) {
        this.x = op.apply(this.x, vec.x, vec2.x);
        this.y = op.apply(this.y, vec.y, vec2.y);
        this.z = op.apply(this.z, vec.z, vec2.z);
        isNan();
    }

    public final void set(BiFunction<Float, Float, Float> op, Vec vec) {
        this.x = op.apply(this.x, vec.x);
        this.y = op.apply(this.y, vec.y);
        this.z = op.apply(this.z, vec.z);
        isNan();
    }

    public void set(Function<Float, Float> op) {
        this.x = op.apply(this.x);
        this.y = op.apply(this.y);
        this.z = op.apply(this.z);
        isNan();
    }

    public Vec diff(Vec pos) {
        return new Vec(this.x - pos.x, this.y - pos.y, this.z - pos.z);
    }

    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    void moveTo(Path2D path2d, ProjectionTransform transform) {
        Vec screen = projectionTransform(transform);
        path2d.moveTo(screen.x, screen.y);
        if (Main.debug_points) {
            System.out.print("\tMoveTo:" + screen.x + "," + screen.y);
        }
    }

    void lineTo(Path2D path2d, ProjectionTransform transform) {
        Vec screen = projectionTransform(transform);
        path2d.lineTo(screen.x, screen.y);
        if (Main.debug_points) {
            System.out.println("\tLineTo:" + screen.x + "," + screen.y);
        }
    }

    void keep_inside(Vec bounds) {
        if (true) {
            this.set((p, b) -> {
                if (p < 1) {
                    p = 2 - p;
                } else if (p > b) {
                    p = 2 * b - p;
                }
                return p;
            }, bounds);
//            this.z = Renderer.Z_PLANE;
        } else {
            float boundsx = bounds.x;
            float boundsy = bounds.y;
            if (this.x < 1) {
                this.x = 2 - this.x;
            } else if (this.x > boundsx) {
                this.x = 2 * boundsx - this.x;
            }

            if (this.y < 1) {
                this.y = 2 - this.y;
            } else {

                if (this.y > boundsy) {
                    this.y = 2 * boundsy - this.y;
                }
            }
        }
    }

    public void check(int x_c, int y_c) {
        if (this.x != x_c || this.y != y_c) {
            System.out.println("get failure: " + this.x + "," + this.y + "!=" + x_c + "," + y_c);
        }
    }

    private Vec projectionTransform(ProjectionTransform transform) {
        if (projection_off) {
            return this;
        }
        return projectionTransform(transform, this);
    }

    private Vec transform(AffineTransform transform) {
        double[] xy = new double[]{this.x, this.y};
        transform.transform(xy, 0, xy, 0, xy.length / 2);
        return new Vec((float) xy[0], (float) xy[1], this.z);
    }

    private Vec inverseTransform(AffineTransform transform) {
        double[] xy = new double[]{this.x, this.y};
        try {
            transform.inverseTransform(xy, 0, xy, 0, xy.length / 2);
        } catch (NoninvertibleTransformException ex) {
            throw new RuntimeException(ex);
        }
        return new Vec((float) xy[0], (float) xy[1], this.z);
    }

    /**
     * return vector that is T*this
     *
     * @param t
     * @return
     */
    static Vec projectionTransform(ProjectionTransform m, Vec input0) {
        if (debug_projection_transform) {
            System.out.print("Input0=" + input0);
        }
        Vec input = input0.transform(m.before);
        if (debug_projection_transform) {
            System.out.print("\tinput=" + input);
        }
        Vec xformed = new Vec();
        xformed.x = input.x * m.m[0][0] + input.y * m.m[1][0] + input.z * m.m[2][0] + m.m[3][0];
        xformed.y = input.x * m.m[0][1] + input.y * m.m[1][1] + input.z * m.m[2][1] + m.m[3][1];
        xformed.z = input.x * m.m[0][2] + input.y * m.m[1][2] + input.z * m.m[2][2] + m.m[3][2];
        float w = input.x * m.m[0][3] + input.y * m.m[1][3] + input.z * m.m[2][3] + m.m[3][3];

        if (w != 0.0f) {
            xformed.x /= w;
            xformed.y /= w;
            xformed.z /= w;
        }

        xformed.x /= xformed.z;
        xformed.y /= xformed.z;
        if (debug_projection_transform) {
            System.out.print("\txformed=" + xformed);
        }
        Vec after = xformed.transform(m.after);
        if (debug_projection_transform) {
            System.out.println("\tafter=" + after);
        }
        return after;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Float.floatToIntBits(this.x);
        hash = 71 * hash + Float.floatToIntBits(this.y);
        hash = 71 * hash + Float.floatToIntBits(this.z);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vec other = (Vec) obj;
        if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x)) {
            return false;
        }
        if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y)) {
            return false;
        }
        if (Float.floatToIntBits(this.z) != Float.floatToIntBits(other.z)) {
            return false;
        }
        return true;
    }

    Shape marker(ProjectionTransform transform) {
        if (mark) {
            Vec screen = this.projectionTransform(transform);
            float marker_size1 = marker_size / screen.z;
            if (Main.debug_points) {
                System.out.println("marking: " + this.x + "," + this.y + "," + this.z + " screen:" + screen.x + "," + screen.y + "," + screen.z + " mark: " + marker_size1);
            }
            return new Rectangle2D.Double(screen.x - marker_size1, screen.y - marker_size1, 2 * marker_size1, 2 * marker_size1);
        }
        return new Path2D.Double();
    }

    /**
     * Creates a transform that maps the bounds into homogeneous coordinates
     * with bounds [(-1,-s),(1,s)] or [(-s,-1),[s,1]) where 0<s<=1
     *
     * @param lower
     * @param upper
     * @return
     */
    public static AffineTransform to1x1(Vec lower, Vec upper) {
        Vec extent = new Vec(lower, upper, (l, u) -> u - l);
        Vec mid = new Vec(lower, upper, (l, u) ->  (u + l) / 2);

        float scale = 2 / Math.max(extent.x, extent.y);

        AffineTransform t = AffineTransform.getScaleInstance(scale, scale);
        //t.translate(mid.x-extent.x / 2, mid.y-extent.y / 2);
        t.translate(-mid.x, -mid.y);
        return t;
    }
    /**
     * Creates a transform that maps coordinates with bounds [(-1,-1),(1,1)] 
     * into homogeneous coordinates with specified bounds 
     *
     * @param lower
     * @param upper
     * @return
     */
    public static AffineTransform from1x1(Vec lower, Vec upper) {
       Vec extent = new Vec(lower, upper, (l, u) -> u - l);
       Vec mid = new Vec(lower, upper, (l, u) ->  (u + l) / 2);

        float scale =  Math.max(extent.x, extent.y)/2;

        AffineTransform t = AffineTransform.getScaleInstance(scale, scale);
        //t.translate(mid.x-extent.x / 2, mid.y-extent.y / 2);
        t.translate(mid.x/scale, mid.y/scale);
        return t;
    }

    public Vec shifted() {
        return new Vec(z,x,y);
    }

    public abstract interface TriFunction<T0, T1, T2, T3> {

        abstract T3 apply(T0 t0, T1 t1, T2 t2);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ')';
    }

    public boolean contains(Vec vec) {
        return all_of((Float p, Float v) -> {
            return v >= 0 && v <= p;
        }, vec);
    }

    private boolean all_of(BiPredicate<Float, Float> bi, Vec v) {
        return bi.test(x, v.x) && bi.test(y, v.y) && bi.test(z, v.z);
    }

    public static void main(String[] args) {
        test(new Vec(-1, -1, 0), new Vec(1, 1, 10));
        test(new Vec(0, 0, 0), new Vec(2, 2, 10));

        test(new Vec(0, 0, 0), new Vec(1, 1, 10));
        test(new Vec(0, 0, 0), new Vec(100, 1, 10));
        test(new Vec(0, 0, 0), new Vec(1, 100, 10));
    }

    private static void test(Vec lower, Vec upper) {
        AffineTransform to1x1 = to1x1(lower, upper);
        AffineTransform from1x1 = from1x1(lower, upper);

        System.out.println("\nlower:" + lower + " upper:" + upper );
        System.out.println(" \nto1x1: " + to1x1+"\t" 
                + lower.transform(to1x1) + "..." + upper.transform(to1x1) 
                + "\t 1x1 origin comes from:" + new Vec(0, 0, 0).inverseTransform(to1x1));
        System.out.println("from1x1:" +from1x1
                + "\t" + lower.transform(to1x1).transform(from1x1) + "..." + upper.transform(to1x1).transform(from1x1)
                + "\t 1x1 origin to:" + new Vec(0, 0, 0).transform(from1x1));

    }
}

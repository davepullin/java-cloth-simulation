package org.pullin.cloth.simulation;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Copyright Dave Pullin. Licensed proprietary property. see
 * http://davepullin.com/license
 */
public class Cube extends Engine {

    public int cloth_height = 30;
    public int cloth_width = 50;
    public int start_y = 20;
    public int spacing = 10;//7;

    private boolean pin_all = true;

    private boolean pin_top = true;
    private boolean pin_bottom = false && pin_all;
    private boolean pin_left = pin_all;
    private boolean pin_right = pin_all;
    private boolean shear = true;
    private boolean sides = true;

    public Cube(Physics physics, Vec bounds) {
        super(physics, bounds);
        int w = Math.min(Main.FRAME_WIDTH, Main.FRAME_HEIGHT) / 4;
        boolean line = false;
        boolean line_only = false;
//        line = true;
//        line_only = true;
        if (line) {
            Point p1 = new Point(new Vec(w, w, Main.Z_PLANE), physics);
            this.points.add(p1);
            Point p2 = new Point(new Vec(3 * w, w, Main.Z_PLANE), physics);
            this.points.add(p2);
            p1.attach(p2, physics.spring_constant);
            if (line_only) {
                return;
            }
        }
        int base = w;// 
        int side = 2 * w;
        int[] point_ends = new int[]{base, base + side};

        for (int x : point_ends) {
            for (int y : point_ends) {
                //     int z = Z_PLANE;
                for (int z : new int[]{Main.Z_PLANE, 2 * Main.Z_PLANE}) {

//z+=3*k2;//push back
                    Point p = newPoint(x, y, z);
                    if (this.points.isEmpty()) {
                        p.pin();
                    } else {
                        if (sides) {
                            attach(p, physics.spring_constant, x - side, y, z);
                            attach(p, physics.spring_constant, x, y - side, z);
                            attach(p, physics.spring_constant, x, y, z - Main.Z_PLANE);
                        }
                        //shear springs ie diagonals
                        if (shear) {
                            attach(p, physics.shear_constant, x - side, y - side, z);
                            attach(p, physics.shear_constant, x - side, y + side, z);
                            attach(p, physics.shear_constant, x, y - side, z - Main.Z_PLANE);
                            attach(p, physics.shear_constant, x, y + side, z - Main.Z_PLANE);
                            attach(p, physics.shear_constant, x, y - side, z + Main.Z_PLANE);
                            attach(p, physics.shear_constant, x, y + side, z + Main.Z_PLANE);
                            attach(p, physics.shear_constant, x - side, y, z - Main.Z_PLANE);
                            attach(p, physics.shear_constant, x - side, y, z + Main.Z_PLANE);
//                    
                        }
                    }
                    p.pin();// fix all for debug
                    this.points.add(p);
                }

            }
        }
    }

    private Integer key(Integer... i) {
        return i[0] + 1000 * i[1] + 1000000 * i[2];
    }

    private Point point_at_xy(Integer... i) {
        return map.get(key(i));
    }

    Map<Integer, Point> map = new HashMap<>();

    private Point newPoint(Integer... i) {

        Point p = point_at_xy(i);
        if (p == null) {
            Vec vec = new Vec(i[0], i[1], i[2]);
            p = new Point(vec, physics);
            map.put(key(i), p);
            System.out.println("point " + vec);
        }
        return p;
    }

    private void attach(Point p, float constant, Integer... i) {
        Vec vec = new Vec(i);
        if (bounds.contains(vec)) {
            Point point = point_at_xy(i);

            if (point != null) {
                if (p.attach(point, constant)) {
                    System.out.println("attach " + p + " to " + point);
                } else {
                    System.out.println("FAILED attach " + p + " to " + point);
                }
            } else {
                System.out.println("point not found " + vec);
            }
        } else {
            System.out.println("outside " + vec);
        }
    }

    public static void main(String[] args) {
        Main.main(args);
    }
}

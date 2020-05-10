package org.pullin.cloth.simulation;

import java.awt.Component;
import javax.swing.JFrame;


/**
 *
 *
 * Copyright Dave Pullin. Licensed proprietary property. see
 * http://davepullin.com/license
 */
public class Main {

    static public boolean projection_off = false;
    static boolean debug_points = false;
    static boolean test_cloth=true;
    public static boolean debug_projection_transform=false;
    public static final int FRAME_HEIGHT = 450;
    public static final int FRAME_WIDTH = 560;
    public static int Z_PLANE = 2;

    public static void main(String[] args) {
        Physics physics = new Physics();
        test_cloth=false;
        if (test_cloth) {
            Cloth.shear = false;
            frame(new Renderer(new Cloth(physics, new Vec(FRAME_WIDTH, FRAME_HEIGHT, Z_PLANE))));
        } else {
            physics.tear_distance = Integer.MAX_VALUE;
            physics.mouse_influence *=100;
            physics.gravity = new Vec();
            frame(new Renderer(new Cube(physics, new Vec(FRAME_WIDTH, FRAME_HEIGHT, Float.POSITIVE_INFINITY))));
        }
        // frame(new Renderer(new Cloth(new Physics(),new Vec(FRAME_WIDTH - 1, FRAME_HEIGHT - 100 - 1, 0f))));

    }

    public static void frame(Component component) {
        JFrame jFrame = new JFrame();
        jFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        jFrame.setLocationRelativeTo(null);

        jFrame.getContentPane().add(component);

        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        AsyncPainter thread = new AsyncPainter(component);
        thread.start();
    }
}

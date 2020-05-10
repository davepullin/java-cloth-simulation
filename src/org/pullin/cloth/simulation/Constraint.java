package org.pullin.cloth.simulation;

import java.awt.geom.Path2D;

/**
 * points p1, p2 are attached by a Hooke's law force on a spring with length
 * rendererPage.spacing.
 *
 * Unless it tears (at rendererPage.tear_distance)
 *
 * based on org.sarath.cloth.simulation
 *
 * removed dependency on rendererPage
 *
 * Copyright Dave Pullin. Licensed proprietary property. see http://davepullin.com/license
 */
public class Constraint {

    private float spring_constant = 0.5f;

    private Point p1;
    private Point p2;
    private float length;
    private Physics physics;

    public Constraint(Point p1, Point p2, float spring_constant, Physics physics) {
        this.p1 = p1;
        this.p2 = p2;
        this.physics = physics;
        this.length = p1.pos.diff(p2.pos).length();// spacing;
        this.spring_constant = spring_constant;

    }

    /**
     * execute movement of p1 and p2 according to the force law
     */
    public void resolve() {
        Vec movement = p1.pos.diff(p2.pos);

        float distance = movement.length();

        if (distance > physics.tear_distance) {
            System.out.println("tear: " + p1 + "," + p2);
            this.p1.remove_constraint(this);
        }
        float factor = distance==0?1:(this.length - distance) / distance;
        movement.set((p) -> p * factor * spring_constant);

        this.p1.pos.set((p, m) -> p + m, movement);
        this.p2.pos.set((p, m) -> p - m, movement);

    }

    public void draw(Path2D path2d,ProjectionTransform transform) {
        if(Main.debug_points)System.out.print("draw:"+p1.pos+"-"+p2.pos+"\t");
        p1.pos.moveTo(path2d,transform);
        p2.pos.lineTo(path2d,transform);
    }
    public static void main(String[] args) {
        Main.main(args);
    }
}

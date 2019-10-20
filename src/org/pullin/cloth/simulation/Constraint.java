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
 * @author root
 */
public class Constraint {

    private float spring_constant = 0.5f;

    private Point p1;
    private Point p2;
    private float length;

    private float tear_distance;

    public Constraint(Point p1, Point p2, float spacing, float spring_constant, float tear_distance) {
        this.p1 = p1;
        this.p2 = p2;

        this.length = p1.pos.diff(p2.pos).length();// spacing;
        this.spring_constant = spring_constant;
        this.tear_distance = tear_distance;
    }

    /**
     * execute movement of p1 and p2 according to the force law
     */
    public void resolve() {
        Vec movement = p1.pos.diff(p2.pos);

        float distance = movement.length();

        if (distance > tear_distance) {
            this.p1.remove_constraint(this);
        }
        float factor = (this.length - distance) / distance;
        movement.set((a) -> a * factor * spring_constant);

        this.p1.pos.set((a, b) -> a + b, movement);
        this.p2.pos.set((a, b) -> a - b, movement);

    }

    public void draw(Path2D path2d) {
        p1.pos.moveTo(path2d);
        p2.pos.lineTo(path2d);
       
    }
}

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
 *  removed dependency on rendererPage
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
       
        this.length = distance();// spacing;
        this.spring_constant=spring_constant;
        this.tear_distance=tear_distance;
    }
    final float distance() {
        float diff_x = this.p1.getX() - this.p2.getX();
        float diff_y = this.p1.getY() - this.p2.getY();
        float dist = (float) Math.sqrt(diff_x * diff_x + diff_y * diff_y);
        return dist;
    }
    /**
     * execute movement of p1 and p2 according to the force law
     */
    public void resolve() {
        float diff_x = this.p1.getX() - this.p2.getX();
        float diff_y = this.p1.getY() - this.p2.getY();
        float dist = (float) Math.sqrt(diff_x * diff_x + diff_y * diff_y);
        float diff = (this.length - dist) / dist;

        if (dist > tear_distance) {
            this.p1.remove_constraint(this);
        }

        float px = diff_x * diff * spring_constant;
        float py = diff_y * diff * spring_constant;

        this.p1.setX(this.p1.getX() + px);
        this.p1.setY(this.p1.getY() + py);
        this.p2.setX(this.p2.getX() - px);
        this.p2.setY(this.p2.getY() - py);
    }


    public void draw(Path2D path2d) {
        path2d.moveTo(this.p1.getX(), this.p1.getY());
        path2d.lineTo(this.p2.getX(), this.p2.getY());
    }
}

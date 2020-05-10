package org.pullin.cloth.simulation;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 *
 * Copyright Dave Pullin. Licensed proprietary property. see
 * http://davepullin.com/license
 */
public class Point {

    Vec pos = new Vec();

    private Vec prev = new Vec();
    private Vec velocity = new Vec();
    private Vec pin = null;

    private ArrayList<Constraint> constraints;

    private Physics physics;

    public Point(float x, float y, Physics physics) {
        this(new Vec(x, y, Main.Z_PLANE), physics);
    }

    public Point(Vec pos, Physics physics) {
        this.pos = new Vec(pos);
        this.prev = new Vec(pos);

        this.constraints = new ArrayList<Constraint>();
        this.physics = physics;
    }

    public void update(Mouse mouse) {
        if (mouse.isDown()) {
            Vec diff = this.pos.diff(mouse.get());
            float dist = diff.length();

            if (mouse.getButton() == 1) {
                if (dist < physics.mouse_influence) {
                    this.prev.set(this.pos);
                    this.pos.set((p, m, mp) -> p - (m - mp) * 1.0f, mouse.get(), mouse.getP());
                }

            } else if (mouse.getButton() == 2) {
                if (dist < physics.mouse_influence) {
                    this.prev.set(this.pos);
//                    Vec from = mouse.getP();
//                    Vec to = mouse.get();
//                    from.shifted();
//                    this.pos.set((p, m, mp) -> p - (m - mp) * 1.0f, new Vec(0f,0f,0.01f), new Vec(0,0,0));
                    this.pos.set((p, m, mp) -> p - (m - mp) * 1.0f,  mouse.get().shifted(), mouse.getP().shifted());
                }
            } else if (dist < physics.mouse_cut) {
                this.constraints = new ArrayList<>();
            }
        }

        this.add_force(physics.gravity);

        Vec newpos = new Vec(pos);
        newpos.set((Float pos1, Float prev1, Float velocity1) -> pos1 + ((pos1 - prev1) * .99f) + ((velocity1 / 2) * physics.delta_squared), this.prev, this.velocity);

        this.prev = pos;
        this.pos = newpos;

        this.velocity.set(0, 0, 0);
    }

    /**
     * draw constraints as a line (probably not required in my use case)
     *
     */
    public void draw(Path2D path2d, ProjectionTransform transform) {
        for_each_constraint((c) -> c.draw(path2d, transform));

        path2d.append(pos.marker(transform), false);
    }

    public void resolve_constraints() {
        if (this.pin != null) {
            this.pos.set(this.pin);
        } else {
            for_each_constraint((c) -> c.resolve());
        }
    }

    private void for_each_constraint(Consumer<Constraint> con) {
        int i = this.constraints.size();
        while (0 != i--) {
            con.accept(this.constraints.get(i));
        }
    }

    /**
     * fix this point where it is
     *
     */
    public void pin() {
        this.pin = new Vec(this.pos);
    }

    /**
     * make a constraint that attach this point to another point
     *
     * @param point
     * @param spring_constant the value of spring_constant
     * @return the boolean
     */
    public boolean attach(Point point, float spring_constant) {
        return point == null ? false : this.constraints.add(new Constraint(this, point, spring_constant, physics));
    }

    public void remove_constraint(Constraint constraint) {
        this.constraints.remove(this.constraints.indexOf(constraint));
    }

    public void add_force(Vec vec) {
        this.velocity.set((a, b) -> a + b, vec);
    }

    public boolean pin(float pinx, float piny) {
        this.pin = new Vec(pinx, piny, Main.Z_PLANE);
        return true;
    }

    public ArrayList<Constraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(ArrayList<Constraint> constraints) {
        this.constraints = constraints;
    }

    @Override
    public String toString() {
        return "Point{" + "pos=" + pos + '}';
    }

}

package org.pullin.cloth.simulation;

import java.util.ArrayList;

/**
 *  * based on org.sarath.cloth.simulation
 *
 * @author root
 */
public class Point {

    Vec pos = new Vec();

    private Vec prev = new Vec();
    private Vec velocity = new Vec();
    private Vec pin = null;

    private ArrayList<Constraint> constraints;
    private Mouse mouse;
    private RendererPage rendererPage;

    public Point(float x, float y, RendererPage rendererPage) {
        this(new Vec(x, y, RendererPage.Z_PLANE), rendererPage);
    }

    public Point(Vec pos, RendererPage rendererPage) {
        this.pos = new Vec(pos);
        this.prev = new Vec(pos);

        this.constraints = new ArrayList<Constraint>();
        this.mouse = rendererPage.getMouse();
        this.rendererPage = rendererPage;
    }

    public void update(float delta) {
        if (rendererPage.mouse.isDown()) {
            Vec diff = this.pos.diff(mouse.get());
            float dist = diff.length();

            if (mouse.getButton() == 1) {
                if (dist < rendererPage.mouse_influence) {
//                    this.prev.set(this.pos.x - (mouse.getX() - mouse.getPx()) * 1.0f,
//                             this.pos.y - (mouse.getY() - mouse.getPy()) * 1.0f);
                    this.prev.set(this.pos);
                    this.prev.set((p,m,mp)->p - (m - mp) * 1.0f,mouse.get(),mouse.getP());
                }

            } else if (dist < rendererPage.mouse_cut) {
                this.constraints = new ArrayList<Constraint>();
            }
        }

        this.add_force(rendererPage.gravity);

        delta *= delta;
        final float fdelta=delta;
        
        Vec newpos = new Vec(pos);
        newpos.set((Float pos1, Float prev1, Float velocity1) -> pos1 + ((pos1 - prev1) * .99f) + ((velocity1 / 2) * fdelta),this.prev,this.velocity);

        this.prev = pos;
        this.pos = newpos;

        this.velocity.set(0, 0, 0);
    }

    /**
     * draw constraints as a line (probably not required in my use case)
     *
     */
    public void draw() {
        if (this.constraints.size() == 0) {
            return;
        }

        int i = this.constraints.size();
        while (0 != i--) {
            this.constraints.get(i).draw(rendererPage.path2d);
        }
    }

    public void resolve_constraints() {
        // this point is in a fixed position
        if (this.pin != null) {
            this.pos.set(this.pin);
            return;
        }

        int i = this.constraints.size();
        while (0 != i--) {
            this.constraints.get(i).resolve();
        }

// dont let points escape the window
this.pos.keep_inside(new Vec(rendererPage.boundsx,rendererPage.boundsy,0));
        
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
        return this.constraints.add(new Constraint(this, point, rendererPage.spacing, spring_constant, rendererPage.tear_distance));
    }

    public void remove_constraint(Constraint constraint) {
        this.constraints.remove(this.constraints.indexOf(constraint));
    }

    public void add_force(Vec vec) {
        this.velocity.set((a, b) -> a + b, vec);
    }

    public boolean pin(float pinx, float piny) {
        this.pin = new Vec(pinx, piny, 0.0F);
        return true;
    }

    public ArrayList<Constraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(ArrayList<Constraint> constraints) {
        this.constraints = constraints;
    }

    public Mouse getMouse() {
        return mouse;
    }

    public void setMouse(Mouse mouse) {
        this.mouse = mouse;
    }



}

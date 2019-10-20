package org.pullin.cloth.simulation;

import java.util.ArrayList;

/**
 *  * based on org.sarath.cloth.simulation
 *
 * @author root
 */
public class Point {

    private Vec2 pos = new Vec2();

    private Vec2 prev = new Vec2();
    private Vec2 velocity = new Vec2();
    private Vec2 pin = null;

    private ArrayList<Constraint> constraints;
    private Mouse mouse;
    private RendererPage rendererPage;

    public Point(float x, float y, RendererPage rendererPage) {
        this(new Vec2(x, y), rendererPage);
    }

    public Point(Vec2 pos, RendererPage rendererPage) {
        this.pos = new Vec2(pos);

        this.prev = new Vec2(pos);
        this.velocity = new Vec2(0, 0);
        this.pin = null;

        this.constraints = new ArrayList<Constraint>();
        this.mouse = rendererPage.getMouse();
        this.rendererPage = rendererPage;
    }

    public void update(float delta) {
        if (rendererPage.mouse.isDown()) {
            Vec2 diff = new Vec2(
                    this.pos.x - mouse.getX(),
                    this.pos.y - mouse.getY());
            float dist = (float) Math.sqrt(diff.x * diff.x + diff.y * diff.y);

            if (mouse.getButton() == 1) {
                if (dist < rendererPage.mouse_influence) {
                    this.prev.set( this.pos.x - (mouse.getX() - mouse.getPx()) * 1.0f
                    , this.pos.y - (mouse.getY() - mouse.getPy()) * 1.0f);
                }

            } else if (dist < rendererPage.mouse_cut) {
                this.constraints = new ArrayList<Constraint>();
            }
        }

        this.add_force(0, rendererPage.gravity);

        delta *= delta;
        Vec2 newpos = new Vec2(
                this.pos.x + ((this.pos.x - this.prev.x) * .99f) + ((this.velocity.x / 2) * delta)
                ,this.pos.y + ((this.pos.y - this.prev.y) * .99f) + ((this.velocity.y / 2) * delta)
        );

        this.prev=pos;
        this.pos=newpos;

        this.velocity.set(0,0);
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
            this.pos.x = this.pin.x;
            this.pos.y = this.pin.y;
            return;
        }

        int i = this.constraints.size();
        while (0 != i--) {
            this.constraints.get(i).resolve();
        }

// dont let points escape the window
        if (this.pos.x > rendererPage.boundsx) {
            this.pos.x = 2 * rendererPage.boundsx - this.pos.x;
        } else {
            if (1 > this.pos.x) {
                this.pos.x = 2 - this.pos.x;
            }
        }
        if (this.pos.y < 1) {
            this.pos.y = 2 - this.pos.y;
        } else {
            if (this.pos.y > rendererPage.boundsy) {
                this.pos.y = 2 * rendererPage.boundsy - this.pos.y;
            }
        }
    }

    /**
     * fix this point where it is
     *
     */
    public void pin() {
        this.pin(this.getX(), this.getY());
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

    public void add_force(float x, float y) {
        this.velocity.x += x;
        this.velocity.y += y;
    }

    public boolean pin(float pinx, float piny) {
        this.pin = new Vec2(pinx, piny);
        return true;
    }

    public float getX() {
        return pos.x;
    }

    public void setX(float x) {
        this.pos.x = x;
    }

    public float getY() {
        return pos.y;
    }

    public void setY(float y) {
        this.pos.y = y;
    }

    public float getPx() {
        return prev.x;
    }

    public void setPx(float px) {
        this.prev.x = px;
    }

    public float getPy() {
        return prev.y;
    }

    public void setPy(float py) {
        this.prev.y = py;
    }

    public float getVx() {
        return velocity.x;
    }

    public void setVx(float vx) {
        this.velocity.x = vx;
    }

    public float getVy() {
        return velocity.y;
    }

    public void setVy(float vy) {
        this.velocity.y = vy;
    }

    public Float getPin_x() {
        return pin.x;
    }

    public void setPin_x(Float pin_x) {
        this.pin.x = pin_x;
    }

    public Float getPin_y() {
        return pin.y;
    }

    public void setPin_y(Float pin_y) {
        this.pin.y = pin_y;
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

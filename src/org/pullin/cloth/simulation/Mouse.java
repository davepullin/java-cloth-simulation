package org.pullin.cloth.simulation;

/**
 *  * based on org.sarath.cloth.simulation
 *
 * @author root
 */

public class Mouse {

    public boolean down = false;
    public int button = 1;
    public int x = 0;
    public int y = 0;
    public int px = 0;
    public int py = 0;

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }

    public Vec get() {
        return new Vec(x,y, 0.0F);
    }
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Vec getP() {
        return new Vec(px,py, 0.0F);
    }
//    public int getPx() {
//        return px;
//    }
//
//    public void setPx(int px) {
//        this.px = px;
//    }
//
//    public int getPy() {
//        return py;
//    }
//
//    public void setPy(int py) {
//        this.py = py;
//    }

}

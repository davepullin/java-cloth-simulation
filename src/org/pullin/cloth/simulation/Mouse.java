package org.pullin.cloth.simulation;

/**
 *  * based on org.sarath.cloth.simulation
 *
 * Copyright Dave Pullin. Licensed proprietary property. see http://davepullin.com/license
 */

public class Mouse {

    public boolean down = false;
    public int button = 1;
    public Vec pos = new Vec();
    public Vec previous = new Vec();


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
        if(this.button==button)
            return;
        System.out.println("Button: "+button);
        this.button = button;
    }

    public Vec get() {
        return pos;
    }
    
//    public int getX() {
//        return pos.x;
//    }
//
//    public void setX(int x) {
//        this.pos.x = x;
//    }
//
//    public int getY() {
//        return pos.y;
//    }
//
//    public void setY(int y) {
//        this.pos.y = y;
//    }

    public Vec getP() {
        return previous;
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

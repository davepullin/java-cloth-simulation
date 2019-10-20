package org.pullin.cloth.simulation;

import java.awt.Component;

/**
 *
 * @author root
 */
public class AsyncPainter extends Thread {

    private Component component;

    public AsyncPainter(Component component) {
        super();
        this.component = component;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
            component.repaint();
        }
    }

}

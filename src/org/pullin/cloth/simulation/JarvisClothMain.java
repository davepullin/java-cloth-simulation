package org.pullin.cloth.simulation;

import javax.swing.JFrame;

/**
 *  * based on org.sarath.cloth.simulation
 *
 * @author root
 */

public class JarvisClothMain {

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.setSize(560, 450);
        jFrame.setLocationRelativeTo(null);
        RendererPage rendererPage = new RendererPage();
        jFrame.getContentPane().add(rendererPage);

        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        WorkerThread thread = new WorkerThread(rendererPage);
        thread.start();
    }
}

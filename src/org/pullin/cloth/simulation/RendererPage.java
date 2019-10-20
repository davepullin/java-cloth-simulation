package org.pullin.cloth.simulation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Path2D;
import javax.swing.JPanel;

/**
 *  * based on org.sarath.cloth.simulation
 *
 * @author root
 */

public class RendererPage extends JPanel implements MouseListener, MouseMotionListener {
    
    public static int Z_PLANE=10;

    public int physics_accuracy = 5;
    public int mouse_influence = 20;
    public int mouse_cut = 5;
    public Vec gravity = new Vec(0,500, 0.0F);
    public int cloth_height = 30;
    public int cloth_width = 50;
    public int start_y = 20;
    public int spacing = 10;//7;
    public float spring_constant = 0.5f;
    public float shear_constant = 0.5f;
    public int tear_distance = 60;
    public Path2D.Float path2d;

    public Mouse mouse;

    public int boundsx;
    public int boundsy;
    public Cloth cloth;

    public RendererPage() {
        setMouse(new Mouse());
        path2d = new Path2D.Float();
        boundsx = 560 - 1;
        boundsy = 350 - 1;

        cloth = new Cloth(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        cloth.update();
        cloth.draw();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        // RenderingHints.VALUE_ANTIALIAS_ON);
        // g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
        // RenderingHints.VALUE_STROKE_PURE);
        g2d.setColor(new Color(8, 8, 8));
        g2d.draw(path2d);

    }

    public Mouse getMouse() {
        return mouse;
    }

    public void setMouse(Mouse mouse) {
        this.mouse = mouse;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouse.button = e.getButton();
        mouse.previous = mouse.pos;
        mouse.pos = new Vec( e.getX(),  e.getY(), 0);
        mouse.down = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouse.down = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
         mouse.previous = mouse.pos;
        mouse.pos = new Vec( e.getX(),  e.getY(), 0f);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
         mouse.previous = mouse.pos;
        mouse.pos = new Vec( e.getX(),  e.getY(), 0f);
        mouse.down = false;
    }

}

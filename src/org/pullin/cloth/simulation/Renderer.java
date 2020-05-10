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
 *
 * Copyright Dave Pullin. Licensed proprietary property. see http://davepullin.com/license
 */
public class Renderer extends JPanel implements MouseListener, MouseMotionListener {

    private static final long serialVersionUID = 1L;

    public Path2D path2d = new Path2D.Float();
 
    public Mouse mouse = new Mouse();

    public Engine engine;
    private final ProjectionTransform projectionTransform;

    public Renderer(Engine engine) {
        this.engine = engine;
        Vec lower = new Vec();
        Vec upper = new Vec(Main.FRAME_WIDTH, Main.FRAME_HEIGHT,Integer.MAX_VALUE);
//        projectionTransform = new ProjectionTransform(
//                FRAME_WIDTH/(float)FRAME_HEIGHT
//                ,AffineTransform.getScaleInstance(1./FRAME_WIDTH, 1./FRAME_HEIGHT)
//                ,AffineTransform.getScaleInstance(FRAME_WIDTH,FRAME_HEIGHT)
//        );
        projectionTransform = new ProjectionTransform(
                Main.FRAME_WIDTH/(float)Main.FRAME_HEIGHT
                ,Vec.to1x1(lower, upper)
                ,Vec.from1x1(lower, upper)
        );
        addMouseListener(this);
        addMouseMotionListener(this);
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        

        engine.draw(mouse, path2d, projectionTransform);
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        // RenderingHints.VALUE_ANTIALIAS_ON);
        // g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
        // RenderingHints.VALUE_STROKE_PURE);
        g2d.setColor(new Color(8, 8, 8));
        g2d.draw(path2d);

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
        mouse.setButton(e.getButton());
        mouse.previous = mouse.pos;
        mouse.pos = new Vec(e.getX(), e.getY(), 0);
        mouse.down = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouse.down = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouse.previous = mouse.pos;
        mouse.pos = new Vec(e.getX(), e.getY(), 0f);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouse.previous = mouse.pos;
        mouse.pos = new Vec(e.getX(), e.getY(), 0f);
        mouse.down = false;
    }
}

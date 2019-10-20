package org.pullin.cloth.simulation;

import java.util.ArrayList;

/**
 *  * based on org.sarath.cloth.simulation
 *
 * @author root
 */
public class Cloth {

    public ArrayList<Point> points;
    private RendererPage rendererPage;

    private boolean pin_top = true;
    private boolean pin_bottom = false;
    private boolean pin_left = false;
    private boolean pin_right = false;
    private boolean shear = true;
    private final int start_x;

    public Cloth(RendererPage rendererPage) {
        points = new ArrayList<Point>();
        this.rendererPage = rendererPage;
        start_x = 560 / 2 - rendererPage.cloth_width * rendererPage.spacing / 2;
        for (float y = 0; y <= rendererPage.cloth_height; y++) {
            for (float x = 0; x <= rendererPage.cloth_width; x++) {
                Point p = new Point(start_x + x * rendererPage.spacing, rendererPage.start_y + y * rendererPage.spacing, rendererPage);
                if (pin_left && x == 0) {
                    p.pin();
                } else {
                    if (pin_right && x == rendererPage.cloth_width) {
                        p.pin();
                    }
                    if (this.points.size() > 0) {
                        p.attach(point_at_xy(x-1,y), rendererPage.spring_constant); //this.points.get(this.points.size() - 1)); // point_at_xy(x-1,y); //
                    }
                }

                if (pin_top && y == 0) { // fix the top
                    p.pin();
                } else {
                    if (pin_bottom && y == rendererPage.cloth_height) {
                        p.pin();
                    }
                    if (y > 0) {
                        p.attach(point_at_xy(x, y - 1), rendererPage.spring_constant);
                    }
                }

                //shear springs ie diagonals
                if (shear) {
                    if (x > 0 && y > 0) {
                        p.attach(point_at_xy(x - 1, y - 1), rendererPage.shear_constant);
                    }
                    if (y > 0) {
                        p.attach(point_at_xy(x + 1, y - 1), rendererPage.shear_constant);
                    }
                }

                this.points.add(p);
            }
        }
    }

    private Point point_at_xy(float x, float y) {
        Point p = this.points.get((int) (x + (y) * (rendererPage.cloth_width + 1)));
        float x_c = start_x + x * rendererPage.spacing;
        float y_c = rendererPage.start_y + y * rendererPage.spacing;
        p.pos.check(x_c,y_c);
        
        return p;
    }

    public void update() {
        int i = rendererPage.physics_accuracy;

        while (0 != i--) {
            int p = this.points.size();
            while (0 != p--) {
                this.points.get(p).resolve_constraints();
            }
        }

        i = this.points.size();
        while (0 != i--) {
            this.points.get(i).update(.026f);
        }
    }

    public void draw() {
        rendererPage.path2d.reset();

        int i = rendererPage.cloth.points.size();
        while (0 != i--) {
            rendererPage.cloth.points.get(i).draw();
        }

    }
}

package org.pullin.cloth.simulation;



/**
 *
 * Copyright Dave Pullin. Licensed proprietary property. see http://davepullin.com/license
 */
public class Cloth extends Engine {

    public int cloth_height = 30;
    public int cloth_width = 50;
    public int start_y = 20;
    public int spacing = 10;//7;

    private boolean pin_all = true;

    private boolean pin_top = true;
    private boolean pin_bottom = false && pin_all;
    private boolean pin_left = pin_all;
    private boolean pin_right = pin_all;
    public static boolean shear = true;
    private final int start_x;

    public Cloth(Physics physics, Vec bounds) {
        super(physics, bounds);

        start_x = Main.FRAME_WIDTH / 2 - this.cloth_width * this.spacing / 2;
        for (int y = 0; y <= this.cloth_height; y++) {
            for (int x = 0; x <= this.cloth_width; x++) {
                Point p = new Point(vecAt(x, y), physics);
                if (pin_left && x == 0) {
                    p.pin();
                } else {
                    if (pin_right && x == this.cloth_width) {
                        p.pin();
                    }
                    p.attach(point_at_xy(x - 1, y), physics.spring_constant);
                }

                if (pin_top && y == 0) {
                    p.pin();
                } else {
                    if (pin_bottom && y == this.cloth_height) {
                        p.pin();
                    }
                    p.attach(point_at_xy(x, y - 1), physics.spring_constant);
                }

                //shear springs ie diagonals
                if (shear) {
                    p.attach(point_at_xy(x - 1, y - 1), physics.shear_constant);
                    p.attach(point_at_xy(x + 1, y - 1), physics.shear_constant);
                }

                this.points.add(p);
            }
        }
    }

    private Point point_at_xy(int x, int y) {
        if (x >= 0 && y >= 0 && x <= this.cloth_width && y <= this.cloth_height) {
            Point p = this.points.get((int) (x + (y) * (this.cloth_width + 1)));
            int x_c = start_x + x * this.spacing;
            int y_c = this.start_y + y * this.spacing;
            p.pos.check(x_c, y_c);
            return p;
        }
        return null;
    }

    private Vec vecAt(int x, int y) {
        return new Vec(start_x + x * this.spacing, this.start_y + y * this.spacing, Main.Z_PLANE);
    }

}

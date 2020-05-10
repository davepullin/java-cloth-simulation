/*
 * Copyright Dave Pullin. Licensed proprietary property. see http://davepullin.com/license
 */
package org.pullin.cloth.simulation;


import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * Copyright Dave Pullin. Licensed proprietary property. see http://davepullin.com/license
 */
public class Engine {
    public Vec bounds;
    
    List<Point> points = new ArrayList<>();
    Physics physics;

    public Engine(Physics physics,Vec upper) {
        this.physics = physics;
        this.bounds = upper;
        Bounds b = new Bounds(new Vec(0,0,0),upper);
        
        
    }
    
    private void update(Mouse mouse) {
        int i = physics.physics_accuracy;
        while (0 != i--) {
            for_each_point((p)->{
                p.resolve_constraints(); 
                p.pos.keep_inside(bounds);
            });
        }
        for_each_point((p)-> p.update(mouse));
    }

    private void draw(Path2D path2d,ProjectionTransform transform) {
        path2d.reset();
        for_each_point((p)-> p.draw(path2d,transform));
    }
    private void for_each_point(Consumer<Point> con) {
        int i = this.points.size();
        while (0 != i--) {
            con.accept(this.points.get(i));
        }
    }

    public void draw(Mouse mouse, Path2D path2d,ProjectionTransform transform) {
        update(mouse);
        
        draw(path2d,transform);
    }
}

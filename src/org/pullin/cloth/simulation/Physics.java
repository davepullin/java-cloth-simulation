/*
 * Copyright Dave Pullin. Licensed proprietary property. see http://davepullin.com/license
 */
package org.pullin.cloth.simulation;

/**
 *
 * Copyright Dave Pullin. Licensed proprietary property. see http://davepullin.com/license
 */
public class Physics {

    public int physics_accuracy = 5;
    public float shear_constant = 0.5F;
    public int mouse_influence = 20;
    public int tear_distance = 60;
    public Vec gravity = new Vec(0, 500, 0.0F);
    public float spring_constant = 0.5F;
    public int mouse_cut = 5;
    public float delta=.026f;
    public float delta_squared= delta *delta;
    
}

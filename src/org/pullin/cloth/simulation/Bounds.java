/*
 * Copyright Dave Pullin. Licensed proprietary property. see http://davepullin.com/license
 */
package org.pullin.cloth.simulation;

import java.awt.geom.AffineTransform;

/**
 *
 * Copyright Dave Pullin. Licensed proprietary property. see http://davepullin.com/license
 */
public class Bounds {

    private final Vec lower;
    private final Vec upper;

    Bounds(Vec lower, Vec upper) {
        this.lower=lower;
        this.upper=upper;
    }
    /**
     * Returns a transform that transforms the bounds into -1 to +1 space
     * @return 
     */
    public AffineTransform to1x1() {
        return Vec.to1x1(lower,upper);
    }
    
    
}

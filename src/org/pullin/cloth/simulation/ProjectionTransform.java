/*
 * Copyright Dave Pullin. Licensed proprietary property. see http://davepullin.com/license
 */
package org.pullin.cloth.simulation;

import java.awt.geom.AffineTransform;

/**
 *
 * Copyright Dave Pullin. Licensed proprietary property. see http://davepullin.com/license
 */
public class ProjectionTransform {

    float[][] m = new float[4][4];
    
    float near = .1f;
    float far = 1000f;
    float fov = 90f;//degrees
    float aspectRatio;
    float fovRad;
    public final AffineTransform before;
    public final AffineTransform after;

    public ProjectionTransform(float aspectRatio, AffineTransform before, AffineTransform after) {
        this.aspectRatio = aspectRatio;
        this.before=before;
        this.after=after;
        
        fovRad = (float) (1f / Math.tan(fov / 2f / 180f * Math.PI));

        m[0][0] = aspectRatio * fovRad;
        m[1][1]=fovRad;
        m[2][2]=far/(far-near);
        m[3][2]=(-far*near)/(far-near);
        m[2][3]=1;
        m[3][3]=0;     
        
    }
    
    
    
    public static void main(String[] args) {
        Main.main(args);
    }
    
}

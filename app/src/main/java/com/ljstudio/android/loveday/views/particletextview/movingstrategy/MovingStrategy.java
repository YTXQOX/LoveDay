package com.ljstudio.android.loveday.views.particletextview.movingstrategy;

import com.ljstudio.android.loveday.views.particletextview.object.Particle;

public abstract class MovingStrategy {
    public abstract void setMovingPath(Particle particle, int rangeWidth, int rangeHeight, double[] targetPosition);
}

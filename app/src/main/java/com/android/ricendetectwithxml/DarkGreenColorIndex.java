package com.android.ricendetectwithxml;

public class DarkGreenColorIndex {

    public float getDGreenColorIndex(float hue, float saturation, float brightness, float r, float g, float b) {
        if ((r + g + b) == 0 || (saturation + brightness) == 0) {
            return 0;
        }
        return hue - (r / (r + g + b)) / (saturation + brightness);
    }
}

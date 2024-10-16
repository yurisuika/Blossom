package dev.yurisuika.blossom.util.config.options.filter;

public class Downfall {

    public float min;
    public float max;

    public Downfall(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

}
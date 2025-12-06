package dev.yurisuika.blossom.util;

public class Filter {

    public Temperature temperature;
    public Downfall downfall;

    public Filter(Temperature temperature, Downfall downfall) {
        this.temperature = temperature;
        this.downfall = downfall;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public Downfall getDownfall() {
        return downfall;
    }

    public void setDownfall(Downfall downfall) {
        this.downfall = downfall;
    }

    public static class Downfall {

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

    public static class Temperature {

        public float min;
        public float max;

        public Temperature(float min, float max) {
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

}
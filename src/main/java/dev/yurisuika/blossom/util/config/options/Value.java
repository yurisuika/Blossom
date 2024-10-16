package dev.yurisuika.blossom.util.config.options;

import dev.yurisuika.blossom.util.config.options.value.Blossoming;
import dev.yurisuika.blossom.util.config.options.value.Fruiting;
import dev.yurisuika.blossom.util.config.options.value.Harvesting;

public class Value {

    public Blossoming blossoming;
    public Fruiting fruiting;
    public Harvesting harvesting;

    public Value(Blossoming blossoming, Fruiting fruiting, Harvesting harvesting) {
        this.blossoming = blossoming;
        this.fruiting = fruiting;
        this.harvesting = harvesting;
    }

    public Blossoming getBlossoming() {
        return blossoming;
    }

    public void setBlossoming(Blossoming blossoming) {
        this.blossoming = blossoming;
    }

    public Fruiting getFruiting() {
        return fruiting;
    }

    public void setFruiting(Fruiting fruiting) {
        this.fruiting = fruiting;
    }

    public Harvesting getHarvesting() {
        return harvesting;
    }

    public void setHarvesting(Harvesting harvesting) {
        this.harvesting = harvesting;
    }

}
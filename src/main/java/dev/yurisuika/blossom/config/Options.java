package dev.yurisuika.blossom.config;

import dev.yurisuika.blossom.util.config.options.Filter;
import dev.yurisuika.blossom.util.config.options.Toggle;
import dev.yurisuika.blossom.util.config.options.Value;
import dev.yurisuika.blossom.util.config.options.filter.Biome;
import dev.yurisuika.blossom.util.config.options.filter.Dimension;
import dev.yurisuika.blossom.util.config.options.filter.Downfall;
import dev.yurisuika.blossom.util.config.options.filter.Temperature;
import dev.yurisuika.blossom.util.config.options.value.Blossoming;
import dev.yurisuika.blossom.util.config.options.value.Fruiting;
import dev.yurisuika.blossom.util.config.options.value.Harvesting;

public class Options {

    public Value value = new Value(
            new Blossoming(0.2F, 10.0D),
            new Fruiting(0.2F, 10.0D),
            new Harvesting(3, 0.5714286F)
    );
    public Filter filter = new Filter(
            new Temperature(-2.0F, 2.0F),
            new Downfall(0.0F, 1.0F),
            new Dimension(new String[]{"minecraft:overworld"}, new String[]{"minecraft:the_nether", "minecraft:the_end"}),
            new Biome(new String[]{"minecraft:forest"}, new String[]{"minecraft:the_void"})
    );
    public Toggle toggle = new Toggle(
            false,
            false
    );

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public Toggle getToggle() {
        return toggle;
    }

    public void setToggle(Toggle toggle) {
        this.toggle = toggle;
    }

}
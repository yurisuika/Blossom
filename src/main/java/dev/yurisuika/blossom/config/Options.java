package dev.yurisuika.blossom.config;

import dev.yurisuika.blossom.util.config.options.Filter;
import dev.yurisuika.blossom.util.config.options.filter.Downfall;
import dev.yurisuika.blossom.util.config.options.filter.Temperature;

public class Options {

    public Filter filter = new Filter(
            new Temperature(-2.0F, 2.0F),
            new Downfall(0.0F, 1.0F)
    );

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

}
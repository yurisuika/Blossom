package dev.yurisuika.blossom.config;

import dev.yurisuika.blossom.util.Filter;

public class Options {

    public Filter filter = new Filter(
            new Filter.Temperature(-2.0F, 2.0F),
            new Filter.Downfall(0.0F, 1.0F)
    );

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

}
package dev.yurisuika.blossom.util.config.options.filter;

public class Biome {

    public String[] whitelist;
    public String[] blacklist;

    public Biome(String[] whitelist, String[] blacklist) {
        this.whitelist = whitelist;
        this.blacklist = blacklist;
    }

    public String[] getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(String[] whitelist) {
        this.whitelist = whitelist;
    }

    public String[] getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(String[] blacklist) {
        this.blacklist = blacklist;
    }

}
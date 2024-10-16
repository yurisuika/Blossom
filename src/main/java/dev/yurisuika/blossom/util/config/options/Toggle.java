package dev.yurisuika.blossom.util.config.options;

public class Toggle {

    public boolean whitelist;
    public boolean blacklist;

    public Toggle(boolean whitelist, boolean blacklist) {
        this.whitelist = whitelist;
        this.blacklist = blacklist;
    }

    public boolean getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(boolean whitelist) {
        this.whitelist = whitelist;
    }

    public boolean getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(boolean blacklist) {
        this.blacklist = blacklist;
    }

}
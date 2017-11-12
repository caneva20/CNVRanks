package me.caneva20.CNVRanks;

import java.util.List;

@SuppressWarnings("CanBeFinal")
public class Rank {
    private String name;
    private String displayName;
    private int order;
    private double neededExp;
    private String tag;
    private String permission;
    private boolean isListable;
    private List<String> permanentPermissions;
    private List<String> temporaryPermissions;

    public Rank(String name, String displayName, int order, long neededExp, String tag, String permission, boolean isListable, List<String> permanentPermissions, List<String> temporaryPermissions) {
        this.name = name;
        this.displayName = displayName;
        this.order = order;
        this.neededExp = neededExp;
        this.tag = tag;
        this.permission = permission;
        this.isListable = isListable;
        this.permanentPermissions = permanentPermissions;
        this.temporaryPermissions = temporaryPermissions;

        CNVRanks.getVault().registerPermission(permission);
    }

    public double getNeededExp() {
        return neededExp;
    }

    public String getTag() {
        return tag;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isListable() {
        return isListable;
    }

    public List<String> getPermanentPermissions() {
        return permanentPermissions;
    }

    public List<String> getTemporaryPermissions() {
        return temporaryPermissions;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "Rank{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", order=" + order +
                ", neededExp=" + neededExp +
                ", tag='" + tag + '\'' +
                ", permission='" + permission + '\'' +
                ", isListable=" + isListable +
                ", permanentPermissions=" + permanentPermissions +
                ", temporaryPermissions=" + temporaryPermissions +
                '}';
    }

    public String getDisplayName() {
        return displayName;
    }
}

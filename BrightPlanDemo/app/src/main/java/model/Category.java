package model;

import com.google.gson.annotations.SerializedName;

public class Category {
    private String name;
    @SerializedName("value")
    private int level;

    public Category(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}

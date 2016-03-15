package model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Profile {
    @SerializedName("profile")
    String name;
    List<Category> categories;

    public Profile(String name, List<Category> categories) {
        this.name = name;
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}

package model;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Portfolio {
    @SerializedName("portfolio")
    ArrayList<Profile> profiles;

    public Portfolio(ArrayList<Profile> profiles) {
        this.profiles = profiles;
    }

    public ArrayList<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(ArrayList<Profile> profiles) {
        this.profiles = profiles;
    }

    public List<Category> getCategoriesFromProfileIndex(int index) {
        if (index <= profiles.size()) {
            return profiles.get(index).getCategories();
        }
        else {
            return new ArrayList<>();
        }
    }
}

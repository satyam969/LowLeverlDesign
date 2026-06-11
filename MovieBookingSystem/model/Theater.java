package model;

import java.util.ArrayList;
import java.util.List;

public class Theater {
    public List<Show> shows;
    public String name;
    public String location;
    public Theater(String name, String location, List<Show> shows){
        this.name = name;
        this.location = location;
        this.shows = (shows != null) ? new ArrayList<>(shows) : new ArrayList<>();
    }
    public void addShow(Show show){
        this.shows.add(show);
    }
}

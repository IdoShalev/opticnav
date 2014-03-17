package opticnav.persistence.web.map;

public class MapsListEntry {
    private String name;
    private int id;

    public MapsListEntry(String name, int id) {
        this.name = name;
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}

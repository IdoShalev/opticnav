package opticnav.ardd.ard;

public class InstanceInfo {
    private String name;
    private int id;

    public InstanceInfo(String name, int id) {
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

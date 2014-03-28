package opticnav.persistence.web;

public class UserARDListEntry {
    private String name;
    private int ard;
    
    public UserARDListEntry(String name, int ard) {
        this.name = name;
        this.ard = ard;
    }

    public String getName() {
        return name;
    }

    public int getArd() {
        return ard;
    }
}

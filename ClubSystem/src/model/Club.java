package model;

public class Club implements Comparable<Club> {
    private String name;
    private String branches;
    private String manager;
    private String location;

    public Club(String name, String branches, String manager, String location) {
        this.name = name;
        this.branches = branches;
        this.manager = manager;
        this.location = location;
    }

    public String getName()     { return name; }
    public String getBranches() { return branches; }
    public String getManager()  { return manager; }
    public String getLocation() { return location; }

    public void setName(String name)         { this.name = name; }
    public void setBranches(String branches) { this.branches = branches; }
    public void setManager(String manager)   { this.manager = manager; }
    public void setLocation(String location) { this.location = location; }

    @Override
    public int compareTo(Club other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    @Override
    public String toString() {
        return name;
    }
}

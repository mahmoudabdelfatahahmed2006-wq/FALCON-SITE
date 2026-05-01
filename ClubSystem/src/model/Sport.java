package model;

public class Sport implements Comparable<Sport> {
    private String name;
    private int id;
    private int numberOfTeams;

    public Sport(String name, int id, int numberOfTeams) {
        this.name = name;
        this.id = id;
        this.numberOfTeams = numberOfTeams;
    }

    public String getName()       { return name; }
    public int getId()            { return id; }
    public int getNumberOfTeams() { return numberOfTeams; }

    public void setName(String name)         { this.name = name; }
    public void setId(int id)                { this.id = id; }
    public void setNumberOfTeams(int n)      { this.numberOfTeams = n; }

    @Override
    public int compareTo(Sport other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    @Override
    public String toString() {
        return name;
    }
}

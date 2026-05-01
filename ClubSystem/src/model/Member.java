package model;

public class Member implements Comparable<Member> {
    private int id;
    private String name;
    private String phone;
    private int numberOfChildren;

    public Member(int id, String name, String phone, int numberOfChildren) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.numberOfChildren = numberOfChildren;
    }

    public int getId()               { return id; }
    public String getName()          { return name; }
    public String getPhone()         { return phone; }
    public int getNumberOfChildren() { return numberOfChildren; }

    public void setId(int id)                         { this.id = id; }
    public void setName(String name)                  { this.name = name; }
    public void setPhone(String phone)                { this.phone = phone; }
    public void setNumberOfChildren(int n)            { this.numberOfChildren = n; }

    @Override
    public int compareTo(Member other) {
        return Integer.compare(this.id, other.id);
    }

    @Override
    public String toString() {
        return "[" + id + "] " + name;
    }
}

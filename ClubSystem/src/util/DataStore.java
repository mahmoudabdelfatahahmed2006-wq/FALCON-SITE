package util;

import model.Club;
import model.Member;
import model.Sport;
import java.util.ArrayList;

/**
 * DataStore — central in-memory store pre-loaded with sample data.
 */
public class DataStore {

    private static DataStore instance;

    private ArrayList<Club>   clubs   = new ArrayList<>();
    private ArrayList<Member> members = new ArrayList<>();
    private ArrayList<Sport>  sports  = new ArrayList<>();

    private DataStore() {
        loadSampleData();
    }

    public static DataStore getInstance() {
        if (instance == null) instance = new DataStore();
        return instance;
    }

    // ── Getters ──────────────────────────────────
    public ArrayList<Club>   getClubs()   { return clubs; }
    public ArrayList<Member> getMembers() { return members; }
    public ArrayList<Sport>  getSports()  { return sports; }

    // ── Sample Data ───────────────────────────────
    private void loadSampleData() {
        // Clubs
        clubs.add(new Club("Zamalek SC",      "12", "Mortada Mansour", "Cairo"));
        clubs.add(new Club("Al Ahly SC",      "20", "Mahmoud El Khatib", "Cairo"));
        clubs.add(new Club("Pyramids FC",     "5",  "Raouf Mana",       "Giza"));
        clubs.add(new Club("Smouha SC",       "3",  "Osama Zaki",       "Alexandria"));
        clubs.add(new Club("El Gouna FC",     "2",  "Sameh Fahmy",      "Hurghada"));

        // Members
        members.add(new Member(1003, "Omar Hassan",   "01012345678", 2));
        members.add(new Member(1001, "Ahmed Ali",     "01098765432", 0));
        members.add(new Member(1005, "Youssef Nabil", "01156789012", 3));
        members.add(new Member(1002, "Sara Mohamed",  "01234567890", 1));
        members.add(new Member(1004, "Nour Ibrahim",  "01187654321", 2));

        // Sports
        sports.add(new Sport("Swimming",   3, 4));
        sports.add(new Sport("Football",   1, 8));
        sports.add(new Sport("Basketball", 2, 6));
        sports.add(new Sport("Tennis",     4, 3));
        sports.add(new Sport("Handball",   5, 5));
    }
}

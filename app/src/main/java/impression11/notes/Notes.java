package impression11.notes;

public class Notes {

    //set variables
    String title;
    String note;
    int id;

    public Notes() {

    }

    public Notes(String title, String note, int id) {

        this.title = title;
        this.note = note;
        this.id = id;

    }

    public String getTitle() {
        return this.title;
    }

    public String getNote() {
        return this.note;
    }

    public int getId() {
        return this.id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {

        return this.title;

    }
}

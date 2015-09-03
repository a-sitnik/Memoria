package memoria.snid1.memoria.database;

import java.util.Date;

public class DAOMem {
    private long id;
    private String note;
    private java.util.Date date;


    @Override
    public String toString() {
        return note;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String toSend() {
        return MemAdapter.dout.format(date) + " - " + note + " [Memoria]";
    }
}
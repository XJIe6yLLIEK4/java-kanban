package model;

import java.util.Objects;

public class Subtask extends Task {
    //String clazz = "Subtask";

    public Subtask(String name, String description, int ID, TaskStatus status) {
        super(name, description, ID, status);
        setClazz("Subtask");
    }
    public Subtask(String name, String description, TaskStatus status) {
        super(name, description, status);
        setClazz("Subtask");
    }

    private int idEpic;

    @Override
    public String toString() {
        return ID + "," +
                "Subtask," +
                name + "," +
                status + "," +
                description + "," +
                idEpic + "," +
                timeToString(startTime) + "," +
                timeToString(getEndTime()) + "," +
                duration;
    }

    public int getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return idEpic == subtask.idEpic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpic);
    }
}

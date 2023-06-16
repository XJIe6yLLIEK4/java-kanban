package model;

public class Subtask extends Task {

    public Subtask(String name, String description, int ID, TaskStatus status) {
        super(name, description, ID, status);
    }

    private int idEpic;

    @Override
    public String toString() {
        return "model.Subtask{" +
                "idEpic=" + idEpic +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ID=" + ID +
                ", status='" + status + '\'' +
                "}";
    }

    public int getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }
}

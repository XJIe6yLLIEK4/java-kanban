public class Subtask extends Task {

    public Subtask(String name, String description, int ID, String status) {
        super(name, description, ID, status);
    }

    private String nameEpic;
    private int idEpic;

    private String getNameEpic() {
        return nameEpic;
    }

    public void setNameEpic(String inEpic) {
        this.nameEpic = inEpic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "inEpic='" + nameEpic + '\'' +
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

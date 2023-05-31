import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> listTasks = new ArrayList<>();

    public Epic(String name, String description, int ID, String status) {
        super(name, description, ID, status);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "listTasks.size =" + listTasks.size() +
                ", status='" + this.getStatus() +
                "} ";
    }

    public void putSubtask(Subtask subtask) {
        listTasks.add(subtask);
    }

    public List<Subtask> getListTasks() {
        return listTasks;
    }

    public Subtask getSubtask (int ID) {
        for (Subtask subtask : listTasks) {
            if (subtask.getID() == ID)
                return subtask;
        }
        return null;
    }

    public void removeSubtask (int ID) {
        listTasks.removeIf(subtask -> subtask.getID() == ID);
    }

    public void updateStatus() {
        boolean isNew = false;
        boolean isDone = false;

        if (!listTasks.isEmpty()) {
            for (Subtask subtask : listTasks)
                if (subtask.getStatus().equals("NEW"))
                    isNew = true;
                else {
                    isNew = false;
                    break;
                }

            for (Subtask subtask : listTasks)
                if (subtask.getStatus().equals("DONE"))
                    isDone = true;
                else {
                    isDone = false;
                    break;
                }
        } else
            this.setStatus("NEW");

        if (isNew)
            this.setStatus("NEW");
        else if (isDone)
            this.setStatus("DONE");
        else
            this.setStatus("IN_PROGRESS");
    }
}

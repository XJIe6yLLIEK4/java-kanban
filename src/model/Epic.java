package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> listTasks = new ArrayList<>();

    public Epic(String name, String description, int ID, TaskStatus status) {
        super(name, description, ID, status);
    }

    @Override
    public String toString() {
        return "model.Epic{" +
                "listTasks.size =" + listTasks.size() +
                ", status='" + status +
                "} ";
    }

    public void putSubtask(Subtask subtask) {
        listTasks.add(subtask);
    }

    public List<Subtask> getListTasks() {
        return listTasks;
    }

    public Subtask getSubtask(int ID) {
        for (Subtask subtask : listTasks) {
            if (subtask.getID() == ID)
                return subtask;
        }
        return null;
    }

    public void removeSubtask(int ID) {
        listTasks.removeIf(subtask -> subtask.getID() == ID);
    }

    public void updateStatus() {
        boolean isNew = true;
        boolean isDone = true;

        if (!listTasks.isEmpty()) {
            for (Subtask subtask : listTasks) {
                if (subtask.getStatus() == TaskStatus.NEW && isNew)
                    isNew = true;
                else
                    isNew = false;

                if (subtask.getStatus() == TaskStatus.DONE && isDone)
                    isDone = true;
                else {
                    isDone = false;
                }
            }
        } else
            this.setStatus(TaskStatus.NEW);

        if (isNew)
            this.setStatus(TaskStatus.NEW);
        else if (isDone)
            this.setStatus(TaskStatus.DONE);
        else
            this.setStatus(TaskStatus.IN_PROGRESS);
    }
}

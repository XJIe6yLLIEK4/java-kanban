package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private List<Subtask> listTasks = new ArrayList<>();
    LocalDateTime endTime;

    public Epic(String name, String description, int ID, TaskStatus status) {
        super(name, description, ID, status);
    }
    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
    }

    @Override
    public String toString() {
        return ID + "," +
                "Epic," +
                name + "," +
                status + "," +
                description + "," +
                " " + "," +
                timeToString(startTime) + "," +
                timeToString(getEndTime()) + "," +
                duration;
    }

    public void putSubtask(Subtask subtask) {
        listTasks.add(subtask);
        updateStatus();
        updateTime();
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

    public LocalDateTime getEndTime() {return endTime;}

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
    
    public void updateTime() {
        if (!listTasks.isEmpty() && listTasks.get(0).startTime != null) {
            LocalDateTime minDate = listTasks.get(0).startTime;
            LocalDateTime maxDate = listTasks.get(0).startTime;
            for (Subtask subtask : listTasks) {
                if (subtask.startTime.isBefore(minDate))
                    minDate = subtask.startTime;

                if (subtask.getEndTime().isAfter(maxDate))
                    maxDate = subtask.getEndTime();
            }
            startTime = minDate;
            duration = Duration.between(minDate, maxDate).toMinutes();
            endTime = startTime.plusMinutes(duration);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(listTasks, epic.listTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), listTasks);
    }
}

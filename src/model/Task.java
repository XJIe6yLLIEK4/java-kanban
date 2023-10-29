package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected String clazz = "Task";
    protected String name;
    protected String description;
    protected int ID;
    protected TaskStatus status;
    protected long duration;
    protected LocalDateTime startTime;
    protected final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");



    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = 0;
        this.startTime = null;
    }

    public Task(String name, String description, int ID, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.ID = ID;
        this.status = status;
        this.duration = 0;
        this.startTime = null;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null)
            return startTime.plusMinutes(duration);
        else
            return null;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return duration;
    }

    static public DateTimeFormatter getFormater() {
        return formatter;
    }

    protected String timeToString(LocalDateTime time) {
        String result;
        if (time != null)
            result = time.format(formatter);
        else
            result = " ";
        return result;
    }

    @Override
    public String toString() {
        return ID + "," +
                "Task," +
                name + "," +
                status + "," +
                description + "," +
                " " + "," +
                timeToString(startTime) + "," +
                timeToString(getEndTime()) + "," +
                duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return ID == task.ID && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, ID, status);
    }
}



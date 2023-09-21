package tests;

import model.Epic;
import model.Subtask;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class EpicTest {

    Epic epic;
    @BeforeEach
    public void CreateEpic() {
        epic = new Epic("TestEpic", "TestEpic", 0, TaskStatus.NEW);
    }

    @Test
    public void EpicStatusIsNewWhenIsEmpty() {
        epic.updateStatus();
        Assertions.assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    public void EpicStatusIsNewWhenAllSubtaskIsNew() {
        epic.putSubtask(new Subtask("TestSubtask", "TestSubtask", 1, TaskStatus.NEW));
        epic.putSubtask(new Subtask("TestSubtask", "TestSubtask", 2, TaskStatus.NEW));
        epic.putSubtask(new Subtask("TestSubtask", "TestSubtask", 3, TaskStatus.NEW));
        epic.updateStatus();
        Assertions.assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    public void EpicStatusIsNewWhenAllSubtaskIsDone() {
        epic.putSubtask(new Subtask("TestSubtask", "TestSubtask", 1, TaskStatus.DONE));
        epic.putSubtask(new Subtask("TestSubtask", "TestSubtask", 2, TaskStatus.DONE));
        epic.putSubtask(new Subtask("TestSubtask", "TestSubtask", 3, TaskStatus.DONE));
        epic.updateStatus();
        Assertions.assertEquals(TaskStatus.DONE, epic.getStatus());
    }

    @Test
    public void EpicStatusIsNewWhenAllSubtaskIsDoneAndNew() {
        epic.putSubtask(new Subtask("TestSubtask", "TestSubtask", 1, TaskStatus.DONE));
        epic.putSubtask(new Subtask("TestSubtask", "TestSubtask", 2, TaskStatus.NEW));
        epic.putSubtask(new Subtask("TestSubtask", "TestSubtask", 3, TaskStatus.DONE));
        epic.updateStatus();
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void EpicStatusIsNewWhenAllSubtaskIsInProgress() {
        epic.putSubtask(new Subtask("TestSubtask", "TestSubtask", 1, TaskStatus.IN_PROGRESS));
        epic.putSubtask(new Subtask("TestSubtask", "TestSubtask", 2, TaskStatus.IN_PROGRESS));
        epic.putSubtask(new Subtask("TestSubtask", "TestSubtask", 3, TaskStatus.IN_PROGRESS));
        epic.updateStatus();
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void EpicTimeIsNullWhenListSubtaskIsEmpty() {
        Assertions.assertNull(epic.getEndTime());
        Assertions.assertNull(epic.getStartTime());
        Assertions.assertEquals(0, epic.getDuration());
    }

    @Test
    public void updateTime() {
        LocalDateTime result = LocalDateTime.MIN.plusMinutes(100);

        Subtask subtask = new Subtask("TestSubtask", "TestSubtask", 1, TaskStatus.IN_PROGRESS);
        subtask.setStartTime(LocalDateTime.MIN);
        subtask.setDuration(30);
        epic.putSubtask(subtask);

        Subtask subtask2 = new Subtask("TestSubtask", "TestSubtask", 1, TaskStatus.IN_PROGRESS);
        subtask2.setStartTime(LocalDateTime.MIN.plusMinutes(60));
        subtask2.setDuration(40);
        epic.putSubtask(subtask2);
        Assertions.assertEquals(result, epic.getEndTime(), "Неправильно посчитано время окончания эпика");
        Assertions.assertEquals(100, epic.getDuration(), "Неправильно рассчитана продолжительность эпика");
    }
}
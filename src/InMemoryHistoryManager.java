import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    @Override
    public List<Task> getHistory() {
        if (history.size() > 10) {
            int countSupertasks = history.size() - 10;
            history.subList(0, (countSupertasks + 1)).clear();
        }
        return history;
    }

    public void add(Task task) {

    }
}

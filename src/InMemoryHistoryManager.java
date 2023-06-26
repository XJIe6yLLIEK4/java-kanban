import model.Node;
import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    CustomLinkedList history = new CustomLinkedList();

    @Override
    public List<Task> getHistory() {
        List<Task> taskList = new ArrayList<>();

        for (Node x = history.head; x != null; x = x.next) {
            taskList.add(x.data);
            }
        return taskList;
    }

    @Override
    public void add(Task task) {
        if (history.mapNode.containsKey(task.getID()))
            history.removeNode(history.mapNode.get(task.getID()));
        history.linkLast(task);
    }

    @Override
    public void remove(int id) {
        history.removeNode(history.mapNode.get(id));
        history.mapNode.remove(id);
    }

    private final class CustomLinkedList  {
        public Map<Integer, Node> mapNode = new HashMap<>();
        public Node head;
        public Node tail;
        int size = 0;

        public void linkLast(model.Task task) {
            final Node t = tail;
            final Node newNode = new Node(task, null, t);
            tail = newNode;
            if (t == null)
                head = newNode;
            else
                t.next = newNode;
            size++;
            mapNode.put(task.getID(), tail);
        }

        public void removeNode(Node node) {
            if (node != null) {
                final model.Task element = node.data;
                final Node next = node.next;
                final Node prev = node.prev;

                if (prev == null) {
                    head = next;
                } else {
                    prev.next = next;
                    node.prev = null;
                }

                if (next == null) {
                    tail = prev;
                } else {
                    next.prev = prev;
                    node.next = null;
                }

                mapNode.remove(node.data.getID());
            }
        }
    }
}

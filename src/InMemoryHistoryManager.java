import model.Node;
import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Map<Integer, Node> mapNode = new HashMap<>();
    CustomLinkedList<Task> history = new CustomLinkedList<>();

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
        if (mapNode.containsKey(task.getID()))
            history.removeNode(mapNode.get(task.getID()));
        history.linkLast(task);
    }

    @Override
    public void remove(int id) {
        history.removeNode(mapNode.get(id));
        mapNode.remove(id);
    }

    public class CustomLinkedList<Task>  {
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

                node.data = null;
                size--;
            }
        }
    }
}

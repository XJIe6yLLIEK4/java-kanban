package model;

public class Node  {

    public Task data;
    public Node next;
    public Node prev;

    public Node(Task data, Node next, Node prev) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
    public Node(Task data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}

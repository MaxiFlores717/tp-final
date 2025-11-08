package backend.base;

import java.util.Iterator;
import backend.interfaces.ILinkedList;

public class SimpleLinkedList<ELEMENT> implements ILinkedList<ELEMENT> {
    //region Node Class
 
    private class Node<ELEMENT> {
        public ELEMENT item;
        public Node<ELEMENT> next;
 
        public Node() {
            this(null, null);
        }
        public Node(ELEMENT item) {
            this(item, null);
        }
        public Node(ELEMENT item, Node<ELEMENT> next) {
            this.item = item;
            this.next = next;
        }
 
        @Override
        public String toString() {
            return this.item.toString();
        }
    }
    //endregion
 
    //region Attributes
 
    protected Node<ELEMENT> head;
    protected int count;
    protected Node<ELEMENT> tail;
    //endregion
 
    //region Constructors
 
    public SimpleLinkedList() {
        this.head = null;
        this.count = 0;
        this.tail = null;
    }
    //endregion
 
    //region Linked List Methods
 
    // Returns the number of elements in this list.
    public int size() {
        return this.count;
    }
 
    public void addFirstRookieVersion(ELEMENT item) {
        if (this.count == 0) {
            this.head = this.tail = new Node<ELEMENT>(item, null);
            ++this.count;
        } else {
            Node<ELEMENT> temp = new Node<ELEMENT>(item, null);
            temp.next = this.head;
            this.head = temp;
            ++this.count;
        }
    }
    // Inserts the specified element at the beginning of this list.
    public void addFirst(ELEMENT item) {
        Node<ELEMENT> temp = new Node<ELEMENT>(item, this.head);
        if (this.count == 0) {
            this.tail = temp;
        }
        this.head = temp;
        ++this.count;
    }
 
    public void addLastRookieVersion(ELEMENT item) {
        if (this.count == 0) {
            this.head = this.tail = new Node<ELEMENT>(item, null);
            ++this.count;
        } else {
            Node<ELEMENT> temp = new Node<ELEMENT>(item, null);
            this.tail.next = temp;
            this.tail = temp;
            ++this.count;
        }
    }
    // Appends the specified element to the end of this list.
    public void addLast(ELEMENT item) {
        Node<ELEMENT> temp = new Node<ELEMENT>(item, null);
        if (this.count == 0) {
            this.head = temp;
        } else {
            this.tail.next = temp;
        }
        this.tail = temp;
        ++this.count;
    }
 
    // Removes and returns the first element from this list.
    public ELEMENT removeFirst() {
        if (this.count == 0) {
            throw new RuntimeException("La lista está vacía...");
        }
        ELEMENT item = this.head.item;
        this.head = this.head.next;
        if (this.head == null) {
            this.tail = null;
        }
        --this.count;
        return item;
    }
 
    // Removes and returns the last element from this list.
    public ELEMENT removeLast() {
        if (this.count == 0) {
            throw new RuntimeException("La lista está vacía...");
        }
        ELEMENT item = this.tail.item;
        if (this.head.next == null) {
            this.head = this.tail = null;
        } else {
            Node<ELEMENT> skip = this.head;
            while (skip.next.next != null) {
                skip = skip.next;
            }
            this.tail = skip;
            this.tail.next = null;
        }
        --this.count;
        return item;
    }

    public void addAtPosition(ELEMENT item, int position) {
    if (position < 0 || position > this.count) {
        System.out.println("posicion fuera de rango");
    }
    Node<ELEMENT> newNode = new Node<>(item, null);

    if (position == 0) { // insertar al principio
        newNode.next = this.head;
        this.head = newNode;
        if (this.count == 0) {
            this.tail = newNode;
        }
    } else if (position == this.count) { // insertar al final
        this.tail.next = newNode;
        this.tail = newNode;
    } else { // insertar en el medio
        Node<ELEMENT> current = this.head;
        for (int i = 0; i < position - 1; i++) {
            current = current.next;
        }
        newNode.next = current.next;
        current.next = newNode;
    }
    this.count++;
  }
  public ELEMENT removeAtPosition(int position) {
    if (this.count == 0) {
        throw new RuntimeException("La lista está vacía...");
    }

    if (position < 0 || position >= this.count) {
        throw new IndexOutOfBoundsException("Posición fuera de rango.");
    }

    ELEMENT removedItem;

    // Caso 1: eliminar el primero
    if (position == 0) {
        removedItem = this.head.item;
        this.head = this.head.next;
        if (this.head == null) {
            this.tail = null;
        }
    }
    // Caso 2: eliminar en el medio o final
    else {
        Node<ELEMENT> current = this.head;
        for (int i = 0; i < position - 1; i++) {
            current = current.next;
        }
        removedItem = current.next.item;
        current.next = current.next.next;
        if (position == this.count - 1) { // eliminó el último
            this.tail = current;
        }
    }

    this.count--;
    return removedItem;
}
    //endregion
 
    //region Object Methods
 
    @Override
    public String toString() {
 
        if (this.size() <=0) {
            return "";
        }
 
        // from https://docs.oracle.com/en/java/javase/15/docs/api/java.base/java/lang/StringBuilder.html
        StringBuilder sb = new StringBuilder();
 
        sb.append("[" + this.head.item.toString());
        for (Node<ELEMENT> skip = this.head.next; skip != null; skip = skip.next) {
            sb.append(", " + skip.item.toString());
        }
        sb.append("]");
 
        return sb.toString();
    }
    //endregion
 
 
    //region Iterable Methods
    @Override
    public Iterator<ELEMENT> iterator() {
        return new SimpleLinkedListIterator(this.head);
    }
 
    private class SimpleLinkedListIterator implements Iterator<ELEMENT> {
        private Node<ELEMENT> current;
 
        public SimpleLinkedListIterator(Node<ELEMENT> current) {
            this.current = current;
        }
 
        @Override
        public boolean hasNext() {
            return this.current != null;
        }
 
        @Override
        public ELEMENT next() {
            if (!this.hasNext()) {
                throw new RuntimeException("La lista está vacía...");
            }
            ELEMENT item = this.current.item;
            this.current = this.current.next;
            return item;
        }
 
    }
 
 
    //endregion
}

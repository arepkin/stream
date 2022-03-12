package ru.liga.interning.homework;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Repkin Andrey {@literal <arepkin@at-consulting.ru>}
 */
public class Node<E> {
    private E data;
    private List<Node<E>> children;

    public Node(E data) {
        this.data = data;
        this.children = new ArrayList<>();
    }

    public E getData() {
        return data;
    }

    public Node<E> addChild(E data) {
        Node<E> toAdd = new Node<>(data);
        children.add(toAdd);
        return toAdd;
    }

    public Stream<Node<E>> stream() {
        //TODO: вернуть поток элементов, который бы соответствовал обходу дерева от корня в глубину
        return null;
    }
}

package ru.liga.interning.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.liga.interning.homework.Node;

import java.util.stream.Collectors;

/**
 * @author Repkin Andrey {@literal <arepkin@at-consulting.ru>}
 */
public class HomeWorkTest {

    //@Test //раскомментить для проверки домашки
    public void traversalTest() {
        Node<String> root = new Node<>("Директор");
        Node<String> a = root.addChild("менеджер");
        a.addChild("аналитик");
        a.addChild("стажёр-аналитик");
        root.addChild("эксперт");
        Node<String> c = root.addChild("секретарь");
        c.addChild("помощник");
        long count = root.stream().count();
        Assertions.assertEquals(count, 7);
        String traversalElems = root.stream().map(Node::getData).collect(Collectors.joining(","));
        Assertions.assertEquals(traversalElems, "Директор,менеджер,аналитик,стажёр-аналитик,эксперт,секретарь,помощник");
    }
}

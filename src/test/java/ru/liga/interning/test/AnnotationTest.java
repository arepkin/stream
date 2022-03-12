package ru.liga.interning.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.liga.interning.annotation.custom.GenderAnnotation;
import ru.liga.interning.annotation.people.Man;
import ru.liga.interning.annotation.people.People;
import ru.liga.interning.annotation.people.Woman;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Repkin Andrey {@literal <arepkin@at-consulting.ru>}
 */
public class AnnotationTest {

    @Test
    public void countManAndWomans() {
        List<People> peoples = new ArrayList<>();
        peoples.add(new Woman());
        peoples.add(new Man());
        peoples.add(new Man());
        final long menCount = peoples.stream()
                .filter(i -> List.of(i.getClass().getAnnotation(GenderAnnotation.class).value()).contains(GenderAnnotation.Gender.MAN))
                .count();
        Assertions.assertEquals(menCount, 2);
        final long womenCount = peoples.stream()
                .filter(i -> List.of(i.getClass().getAnnotation(GenderAnnotation.class).value()).contains(GenderAnnotation.Gender.WOMAN))
                .count();
        Assertions.assertEquals(womenCount, 1);
    }
}

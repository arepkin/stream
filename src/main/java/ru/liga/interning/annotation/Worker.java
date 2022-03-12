package ru.liga.interning.annotation;

/**
 * @author Repkin Andrey {@literal <arepkin@at-consulting.ru>}
 */
@FunctionalInterface
public interface Worker {
    Integer summator(Integer a, Integer b);
}

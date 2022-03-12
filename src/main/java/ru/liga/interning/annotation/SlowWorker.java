package ru.liga.interning.annotation;

/**
 * @author Repkin Andrey {@literal <arepkin@at-consulting.ru>}
 */
public class SlowWorker implements Worker {

    @Override
    public Integer summator(Integer a, Integer b) {
        return a + b;
    }

    @Deprecated
    public Integer oldSummator(Integer a, Integer b) {
        return a + b;
    }
}

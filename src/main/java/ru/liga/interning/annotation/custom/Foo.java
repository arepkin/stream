package ru.liga.interning.annotation.custom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Repkin Andrey {@literal <arepkin@at-consulting.ru>}
 */
@Target(ElementType.ANNOTATION_TYPE)
@Repeatable(FooContainer.class)
@interface Foo {
}

@Target(ElementType.ANNOTATION_TYPE)
@interface FooContainer {
    Foo[] value();
}




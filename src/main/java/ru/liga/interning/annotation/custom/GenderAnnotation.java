package ru.liga.interning.annotation.custom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@FooContainer //illegal, because value hasn't "default"
//@FooContainer({})
//@FooContainer(@Foo)
//@FooContainer({@Foo, @Foo})
//@Foo @Foo
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME) //из-за наличия SOURCE эта аннотация исчезнет в decompiled .class файле People.class
public @interface GenderAnnotation {
    enum Gender {
        MAN, WOMAN
    }

    Gender[] value() default {Gender.MAN};
}

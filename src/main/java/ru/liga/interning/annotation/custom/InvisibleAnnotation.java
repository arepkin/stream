package ru.liga.interning.annotation.custom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE) //из-за наличия SOURCE эта аннотация исчезнет в decompiled .class файле .class
public @interface InvisibleAnnotation {
}

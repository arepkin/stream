package ru.liga.interning.annotation.people;

import ru.liga.interning.annotation.custom.GenderAnnotation;

/**
 * @author Repkin Andrey {@literal <arepkin@at-consulting.ru>}
 */
@GenderAnnotation(GenderAnnotation.Gender.MAN)
//@GenderAnnotation //also good
public class Man implements People {
}

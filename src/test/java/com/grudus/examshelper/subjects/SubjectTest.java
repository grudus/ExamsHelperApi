package com.grudus.examshelper.subjects;

import com.grudus.examshelper.exceptions.InvalidColorException;
import org.junit.Test;

import static com.grudus.examshelper.utils.Utils.randAlph;
import static com.grudus.examshelper.utils.Utils.randomColor;
import static org.junit.Assert.assertEquals;

public class SubjectTest {

    @Test
    public void shouldAssertValidColor() {
        String validColor = "#a1ff45";

        Subject.assertColor(validColor);
    }

    @Test
    public void shouldAssertValidShortenColor() {
        String validColor = "#a9c";

        Subject.assertColor(validColor);
    }

    @Test(expected = InvalidColorException.class)
    public void shouldThrowExceptionWhenColorWithoutHashTag() {
        String invalidColor = "123456";

        Subject.assertColor(invalidColor);
    }

    @Test(expected = InvalidColorException.class)
    public void shouldThrowExceptionWhenShortenColorWithoutHashTag() {
        String invalidColor = "5ca";

        Subject.assertColor(invalidColor);
    }

    @Test
    public void shouldThrowExceptionWhenColorHasInvalidLength() {
        String[] invalidColors = {"#a", "#aa", "#aaaa", "#aaaaa", "#aaaaaaa", "#aaaaaaaa"};
        int numberOfInvalidColors = 0;

        for (String color : invalidColors) {
            try {
                Subject.assertColor(color);
            } catch (InvalidColorException e) {
                numberOfInvalidColors++;
            }
        }
        assertEquals(invalidColors.length, numberOfInvalidColors);
    }

    @Test(expected = InvalidColorException.class)
    public void shouldThrowExceptionWhenInvalidCharacters() {
        String invalidColor = "#a1eqyz";

        Subject.assertColor(invalidColor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateInstanceWhenLabelTooShort() {
        String label = randAlph(Subject.MAX_LABEL_LENGTH + 1);

        new Subject(5L, label, randomColor());
    }

}
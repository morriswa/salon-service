package org.morriswa.salon.utility;

import org.junit.jupiter.api.Test;
import org.morriswa.salon.validation.StrTools;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class StrToolsTest {

    @Test
    void blankStringHasNoValue() {
        final String blank = " ";
        final boolean blankIsNotNull = StrTools.isNotNullButBlank(blank);
        final boolean blankHasValue = StrTools.hasValue(blank);

        assertFalse("blank is not expected to have a value", blankHasValue);
        assertTrue("blank is expected to be initialized", blankIsNotNull);
    }

    @Test
    void longBlankStringHasNoValue() {
        final String blank = " ".repeat(50);
        final boolean blankIsNotNull = StrTools.isNotNullButBlank(blank);
        final boolean blankHasValue = StrTools.hasValue(blank);

        assertFalse("blank is not expected to have a value", blankHasValue);
        assertTrue("blank is expected to be initialized", blankIsNotNull);
    }

    @Test
    void blankStringWithSpecialWhitespaceHasNoValue() {
        final String blank = "\t\n".repeat(50);
        final boolean blankIsNotNull = StrTools.isNotNullButBlank(blank);
        final boolean blankHasValue = StrTools.hasValue(blank);

        assertFalse("blank is not expected to have a value", blankHasValue);
        assertTrue("blank is expected to be initialized", blankIsNotNull);
    }

    @Test
    void emptyStringHasNoValue() {
        final String empty = "";
        final boolean emptyIsNotNull = StrTools.isNotNullButBlank(empty);
        final boolean emptyHasValue = StrTools.hasValue(empty);

        assertFalse("empty is not expected to have a value", emptyHasValue);
        assertTrue("empty is expected to be initialized", emptyIsNotNull);
    }

    @Test
    void nullStringHasNoValue() {
        final String uninitializedString = null;
        final boolean uninitializedStringIsNotNull = StrTools.isNotNullButBlank(uninitializedString);
        final boolean uninitializedStringHasValue = StrTools.hasValue(uninitializedString);

        assertFalse("null is not expected to have a value", uninitializedStringHasValue);
        assertFalse("null is not expected to be initialized", uninitializedStringIsNotNull);
    }
}

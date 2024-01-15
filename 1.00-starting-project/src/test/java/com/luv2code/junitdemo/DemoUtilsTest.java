package com.luv2code.junitdemo;

import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class DemoUtilsTest {

    DemoUtils demoUtils;

    @BeforeEach
    void beforeEach() {
        demoUtils = new DemoUtils();
    }

    @Test
    @DisplayName("Equals and Not Equals")
    void testEqualsAndNotEquals() {

        assertEquals(6, demoUtils.add(2,4), "2+4 must be 6");
        assertNotEquals(6, demoUtils.add(1,9), "1+9 must not be 6");
    }

    @Test
    @DisplayName("Null and Not Null")
    void testNullAndNotNull() {

        String str1 = null;
        String str2 = "brandon";

        assertNull(demoUtils.checkNull(str1), "Object should be null");
        assertNotNull(demoUtils.checkNull(str2), "Object should not be null");
    }

    @Test
    @DisplayName("Same and Not Same")
    void testSameAndNotSame() {
        String str = "brandons";

        assertSame(demoUtils.getAcademy(), demoUtils.getAcademyDuplicate(), "Object should be the same");
        assertNotSame(str, demoUtils.getAcademy(), "Object should be the same");
    }

    @Test
    @DisplayName("True and False")
    void testTrueFalse() {
        int i = 10;
        int y = 5;

        assertTrue(demoUtils.isGreater(i,y), "Return true");
        assertFalse(demoUtils.isGreater(y,i), "Return false");
    }

    @Test
    @DisplayName("Array Equals")

    void testArrayEquals() {
        String[] stringArray = {"A", "B", "C"};

        assertArrayEquals(stringArray, demoUtils.getFirstThreeLettersOfAlphabet(), "Array should be the same");
    }

    @Test
    @DisplayName("Iterable Equals")
    void testIterableEquals() {
        List<String> list = List.of("bra", "n", "don");

        assertIterableEquals(list, demoUtils.getAcademyInList(), "Iterable should be the same");
    }

    @Test
    @DisplayName("Lines Match")
    void testLinesMatch() {
        List<String> list = List.of("bra", "n", "don");

        assertLinesMatch(list, demoUtils.getAcademyInList(), "Lines should match");
    }

    @Test
    @DisplayName("Throws and Does Not Throw")
    void testThrowsAndDoesNotThrow() {
        assertThrows(Exception.class, () -> { demoUtils.throwException(-1); }, "Should throw exception");
        assertDoesNotThrow(() -> { demoUtils.throwException(1); }, "Should throw exception");
    }

    @Test
    @DisplayName("Timeout")
    void testTimeout() {
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {demoUtils.checkTimeout();}, "Method execution exceeded the time limit");
    }

    @Test
    @DisplayName("Multiply")
    void testMultiply() {
        assertEquals(12, demoUtils.multiply(4,3), "4*3 must be 12");
    }

}

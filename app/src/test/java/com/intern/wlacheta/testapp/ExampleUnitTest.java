package com.intern.wlacheta.testapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void stringFormatTest() {
        String messageFormat = "ID: %d  StartDate: %s";
        String startDate = "27.10.2018";
        int id = 5;
        System.out.print(String.format(messageFormat,id,startDate));
    }
}
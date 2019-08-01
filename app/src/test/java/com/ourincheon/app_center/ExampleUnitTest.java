package com.ourincheon.app_center;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);     //두 값이 같아야 한다고 주장, 값 자체를 비교(assertSame은 객체 포인터 비교)
    }
}
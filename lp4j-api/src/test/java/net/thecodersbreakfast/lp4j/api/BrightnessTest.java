package net.thecodersbreakfast.lp4j.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BrightnessTest {

    @Test
    public void valueOf() {
        Brightness brightness = Brightness.of(Brightness.MIN_VALUE);
        assertNotNull(brightness);
        assertEquals(Brightness.MIN_VALUE, brightness.getBrightness());
    }

    @Test(expected = IllegalArgumentException.class)
    public void valueOf_tooLow() {
        Brightness.of(Brightness.MIN_VALUE - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void valueOf_tooHigh() {
        Brightness.of(Brightness.MAX_VALUE + 1);
    }


}

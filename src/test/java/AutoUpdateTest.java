import io.github.zebMcCorkle.FontInstaller.AutoUpdate;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/*
 * Copyright (c) 2016 Zeb McCorkle
 *
 * All Rights Reserved
 */
public class AutoUpdateTest {
    @Test
    public void hasUpdate() throws NoSuchFieldException, IllegalAccessException {
        Field VERSION = AutoUpdate.class.getField("VERSION");
        VERSION.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(VERSION, VERSION.getModifiers() & ~Modifier.FINAL);
        VERSION.set(null, "wontmatch");

        AutoUpdate updater = new AutoUpdate();
        updater.run();

        assertFalse(updater.uptodate);
    }

    @Test
    public void consistent() {
        AutoUpdate updater = new AutoUpdate();
        updater.run();
        boolean previous = updater.uptodate;

        for (int i = 0; i < 25; i++) {
            updater.run();
            assertEquals(previous, (previous = updater.uptodate));
        }
    }
}

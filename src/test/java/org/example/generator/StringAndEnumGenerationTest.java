package org.example.generator;

import org.example.classes.MyThread;
import org.example.classes.State;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringAndEnumGenerationTest {
    private final Generator generator = new Generator();

    @Test
    void generatesStringTest() throws ReflectiveOperationException {
        String result = generator.generateValueOfType(String.class);

        assertNotNull(result);
    }

    @Test
    void generatesStateConstantTest() throws ReflectiveOperationException {
        State result = generator.generateValueOfType(State.class);

        assertNotNull(result);
        assertTrue(EnumSet.allOf(State.class).contains(result));
    }

    @Test
    void generatesObjectWithEnumConstructorParameterTest() throws ReflectiveOperationException {
        MyThread result = generator.generateValueOfType(MyThread.class);

        assertNotNull(result);
        assertNotNull(result.getState());
    }
}

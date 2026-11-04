package org.example.generator;

import org.example.classes.Rectangle;
import org.example.classes.State;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Array;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArrayGenerationTest {

    private final Generator generator = new Generator();

    @ParameterizedTest(name = "{0} -> elements: {1}")
    @MethodSource("primitiveAndWrapperArrays")
    void generatesPrimitiveAndWrapperArrayTest(Class<?> arrayType, Class<?> elementType) throws ReflectiveOperationException {

        assertGeneratedArray(arrayType, elementType);
    }

    @ParameterizedTest(name = "{0} -> elements: {1}")
    @MethodSource("otherArrays")
    void generatesStringEnumAndUserObjectArrayTest(Class<?> arrayType, Class<?> elementType) throws ReflectiveOperationException {

        assertGeneratedArray(arrayType, elementType);
    }

    private void assertGeneratedArray(Class<?> arrayType, Class<?> elementType) throws ReflectiveOperationException {
        Object result = generator.generateValueOfType(arrayType);

        assertInstanceOf(arrayType, result);

        int length = Array.getLength(result);
        assertTrue(length > 0);

        for (int i = 0; i < length; i++) {
            assertInstanceOf(elementType, Array.get(result, i));
        }
    }

    static Stream<Arguments> primitiveAndWrapperArrays() {
        return Stream.of(
                Arguments.of(byte[].class, Byte.class),
                Arguments.of(Byte[].class, Byte.class),
                Arguments.of(short[].class, Short.class),
                Arguments.of(Short[].class, Short.class),
                Arguments.of(int[].class, Integer.class),
                Arguments.of(Integer[].class, Integer.class),
                Arguments.of(long[].class, Long.class),
                Arguments.of(Long[].class, Long.class),
                Arguments.of(float[].class, Float.class),
                Arguments.of(Float[].class, Float.class),
                Arguments.of(double[].class, Double.class),
                Arguments.of(Double[].class, Double.class),
                Arguments.of(boolean[].class, Boolean.class),
                Arguments.of(Boolean[].class, Boolean.class),
                Arguments.of(char[].class, Character.class),
                Arguments.of(Character[].class, Character.class)
        );
    }

    static Stream<Arguments> otherArrays() {
        return Stream.of(
                Arguments.of(String[].class, String.class),
                Arguments.of(State[].class, State.class),
                Arguments.of(Rectangle[].class, Rectangle.class)
        );
    }
}

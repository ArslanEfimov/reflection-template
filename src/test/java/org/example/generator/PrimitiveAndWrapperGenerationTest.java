package org.example.generator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class PrimitiveAndWrapperGenerationTest {

    private final Generator generator = new Generator();

    @ParameterizedTest(name = "[{index}] {0} -> {1}")
    @MethodSource("primitiveAndWrapperTypes")
    void generatesExpectedWrapperTest(Class<?> requestedType, Class<?> expectedWrapper) throws ReflectiveOperationException {
        Object result = generator.generateValueOfType(requestedType);

        assertInstanceOf(expectedWrapper, result);
    }

    private static Stream<Arguments> primitiveAndWrapperTypes() {
        return Stream.of(
                Arguments.of(byte.class, Byte.class),
                Arguments.of(Byte.class, Byte.class),
                Arguments.of(short.class, Short.class),
                Arguments.of(Short.class, Short.class),
                Arguments.of(int.class, Integer.class),
                Arguments.of(Integer.class, Integer.class),
                Arguments.of(long.class, Long.class),
                Arguments.of(Long.class, Long.class),
                Arguments.of(float.class, Float.class),
                Arguments.of(Float.class, Float.class),
                Arguments.of(double.class, Double.class),
                Arguments.of(Double.class, Double.class),
                Arguments.of(boolean.class, Boolean.class),
                Arguments.of(Boolean.class, Boolean.class),
                Arguments.of(char.class, Character.class),
                Arguments.of(Character.class, Character.class)
        );
    }
}

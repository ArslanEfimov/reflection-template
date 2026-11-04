package org.example.generator;

import org.example.annotations.Generatable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FailureGenerationTest {

    private final Generator generator = new Generator();

    @Test
    void rejectsUserClassWithoutAnnotationTest() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> generator.generateValueOfType(UnannotatedClass.class)
        );

        assertTrue(exception.getMessage().contains("@Generatable"));
        assertTrue(exception.getMessage().contains(UnannotatedClass.class.getName()));
    }

    @ParameterizedTest
    @ValueSource(classes = {
            UnannotatedInterface.class,
            UnannotatedAbstractClass.class
    })
    void rejectsAbstractTypeWithoutAnnotationTest(Class<?> requestedType) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> generator.generateValueOfType(requestedType)
        );

        assertTrue(exception.getMessage().contains("@Generatable"));
        assertTrue(exception.getMessage().contains(requestedType.getName()));
    }

    @Test
    void rejectsEmptyImplementationListTest() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> generator.generateValueOfType(NoImplementations.class)
        );

        assertTrue(exception.getMessage().contains("No implementations"));
        assertTrue(exception.getMessage().contains(NoImplementations.class.getName()));
    }

    @ParameterizedTest
    @ValueSource(classes = {
            InterfaceCandidate.class,
            AbstractCandidate.class
    })
    void rejectsNonConcreteImplementationTest(Class<?> requestedType) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> generator.generateValueOfType(requestedType)
        );

        assertTrue(exception.getMessage().contains(requestedType.getName()));
    }

    @Test
    void rejectsEmptyEnumTest() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> generator.generateValueOfType(EmptyEnum.class)
        );

        assertTrue(exception.getMessage().contains(EmptyEnum.class.getName()));
    }

    public static class UnannotatedClass {
    }

    public interface UnannotatedInterface {
    }

    public static abstract class UnannotatedAbstractClass {
    }

    @Generatable
    public interface NoImplementations {
    }

    @Generatable(implementations = {InterfaceCandidate.class})
    public interface InterfaceCandidate {
    }

    @Generatable(implementations = {AbstractCandidate.class})
    public static abstract class AbstractCandidate {
    }

    public enum EmptyEnum {
    }
}

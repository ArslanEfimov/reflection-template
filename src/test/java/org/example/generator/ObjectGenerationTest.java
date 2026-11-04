package org.example.generator;

import org.example.classes.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class ObjectGenerationTest {

    private final Generator generator = new Generator();

    @ParameterizedTest
    @ValueSource(classes = {
            Example.class,
            Rectangle.class,
            Triangle.class
    })
    void generatesTemplateObjectTest(Class<?> requestedType) throws ReflectiveOperationException {
        Object result = generator.generateValueOfType(requestedType);

        assertNotNull(result);
        assertInstanceOf(requestedType, result);
    }

    @Test
    void generatesProductWithAnyAvailableConstructorTest() throws ReflectiveOperationException {
        Product product = generator.generateValueOfType(Product.class);

        assertNotNull(product);
        assertNotNull(product.getName());
    }

    @Test
    void generatesNewClassWithNestedObjectTest() throws ReflectiveOperationException {
        CompositeClass compositeClass = generator.generateValueOfType(CompositeClass.class);

        assertNotNull(compositeClass);
        assertNotNull(compositeClass.getTitle());
        assertNotNull(compositeClass.getQuantity());
        assertNotNull(compositeClass.getRectangle());
    }

    @Test
    void invokesConstructorWithoutParametersTest() throws ReflectiveOperationException {
        WithoutArgumentsConstructorClass result = generator.generateValueOfType(WithoutArgumentsConstructorClass.class);
        assertNotNull(result);
    }
}

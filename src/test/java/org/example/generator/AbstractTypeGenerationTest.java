package org.example.generator;

import org.example.classes.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AbstractTypeGenerationTest {

    private final Generator generator = new Generator();

    @Test
    void generatesShapeImplementationTest() throws ReflectiveOperationException {
        Shape result = generator.generateValueOfType(Shape.class);

        assertNotNull(result);
        assertTrue(result instanceof Rectangle || result instanceof Triangle);
    }

    @Test
    void generatesAbstractClassImplementationTest() throws ReflectiveOperationException {
        Document result = generator.generateValueOfType(Document.class);

        TextDocument document = assertInstanceOf(TextDocument.class, result);

        assertNotNull(document.getTitle());
        assertNotNull(document.getContent());
    }
}

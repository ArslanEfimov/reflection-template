package org.example.generator;

import org.example.classes.Cart;
import org.example.classes.Catalog;
import org.example.classes.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CollectionGenerationTest {

    private final Generator generator = new Generator();

    @ParameterizedTest
    @ValueSource(classes = {List.class, ArrayList.class})
    void generatesIntegerListTest(Class<?> requestedType) throws ReflectiveOperationException {
        Object result = generator.generateValueOfType(requestedType);

        assertInstanceOf(requestedType, result);

        List<?> items = assertInstanceOf(List.class, result);
        assertFalse(items.isEmpty());

        for (Object item : items) {
            assertInstanceOf(Integer.class, item);
        }
    }

    @ParameterizedTest
    @ValueSource(classes = {Map.class, HashMap.class})
    void generatesStringToIntegerMapTest(Class<?> requestedType) throws ReflectiveOperationException {
        Object result = generator.generateValueOfType(requestedType);

        assertInstanceOf(requestedType, result);

        Map<?, ?> entries = assertInstanceOf(Map.class, result);
        assertFalse(entries.isEmpty());

        for (Map.Entry<?, ?> entry : entries.entrySet()) {
            assertInstanceOf(String.class, entry.getKey());
            assertInstanceOf(Integer.class, entry.getValue());
        }
    }

    @Test
    void generatesProductListFromConstructorTest() throws ReflectiveOperationException {
        Cart cart = generator.generateValueOfType(Cart.class);

        assertNotNull(cart);
        assertNotNull(cart.getItems());
        assertFalse(cart.getItems().isEmpty());

        for (Object item : cart.getItems()) {
            Product product = assertInstanceOf(Product.class, item);
            assertNotNull(product.getName());
        }
    }

    @Test
    void generatesProductMapFromConstructorTest() throws ReflectiveOperationException {
        Catalog catalog = generator.generateValueOfType(Catalog.class);

        assertNotNull(catalog);
        assertNotNull(catalog.getProducts());
        assertFalse(catalog.getProducts().isEmpty());

        Map<?, ?> entries = catalog.getProducts();

        for (Map.Entry<?, ?> entry : entries.entrySet()) {
            assertInstanceOf(String.class, entry.getKey());

            Product product = assertInstanceOf(Product.class, entry.getValue());

            assertNotNull(product.getName());
        }
    }
}

package org.example.classes;

import org.example.annotations.Generatable;

import java.util.Map;

@Generatable
public class Catalog {

    private final Map<String, Product> products;

    public Catalog(Map<String, Product> products) {
        this.products = products;
    }

    public Map<String, Product> getProducts() {
        return products;
    }

}

package org.example.classes;

import org.example.annotations.Generatable;

@Generatable
public class CompositeClass {

    private final String title;
    private final Integer quantity;
    private final Rectangle rectangle;

    public CompositeClass(String title, Integer quantity, Rectangle rectangle) {
        this.title = title;
        this.quantity = quantity;
        this.rectangle = rectangle;
    }

    public String getTitle() {
        return title;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}

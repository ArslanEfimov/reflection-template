package org.example.classes;

import org.example.annotations.Generatable;

@Generatable(implementations = {TextDocument.class})
public abstract class Document {

    public abstract String getTitle();
}

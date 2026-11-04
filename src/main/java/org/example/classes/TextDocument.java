package org.example.classes;

import org.example.annotations.Generatable;

@Generatable
public class TextDocument extends Document{
    private final String title;
    private final String content;

    public TextDocument(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getContent(){
        return content;
    }
}

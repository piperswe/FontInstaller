package io.github.zebMcCorkle.FontInstaller;

/*
 * Copyright (c) 2016 Zeb McCorkle
 *
 * All Rights Reserved
 */
public class Font implements Comparable<Font> {
    private FontType type;
    private String name;

    public Font(FontType type, String name) {
        this.type = type;
        this.name = name;
    }

    public FontType getFontType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() { return getName(); }

    @Override
    public int compareTo(Font f) {
        return toString().compareToIgnoreCase(f.toString());
    }
}

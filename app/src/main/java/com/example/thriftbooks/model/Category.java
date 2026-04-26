package com.example.thriftbooks.model;

public class Category {
    private String id;
    private String name;
    private int iconRes;

    public Category(String id, String name, int iconRes) {
        this.id = id;
        this.name = name;
        this.iconRes = iconRes;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getIconRes() { return iconRes; }
}

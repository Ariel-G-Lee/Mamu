package com.example.mp_project;

public class SampleData {
    private int icon;
    private String name;
    private String content;

    public SampleData(int icon, String name, String content){
        this.icon = icon;
        this.name = name;
        this.content = content;
    }

    public int geticon()
    {
        return this.icon;
    }

    public String getname()
    {
        return this.name;
    }

    public String getcontent()
    {
        return this.content;
    }
}
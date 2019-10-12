package com.example.procontexttestwork;

public class JsonInfoClass {
    private int id;
    private String name;

    public JsonInfoClass (int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }

    public void setName(String newName){
        name = newName;
    }
}

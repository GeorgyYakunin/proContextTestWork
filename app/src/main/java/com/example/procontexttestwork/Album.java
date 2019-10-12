package com.example.procontexttestwork;

public class Album extends JsonInfoClass{
     private int id;
     private int photographerId;
     private String name;

     public Album(int id, String name, int photographerId){
          super(id, name);
          this.photographerId = photographerId;
     }

     public int getPhotographerId(){
         return photographerId;
     }

}

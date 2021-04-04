// package edu.ucalgary.ensf409;

//Manufacturing class
public class Manufacturer {
  //Class fields
  private String id;
  private String name;

  //Class constructor
  public Manufacturer (String id, String name, String phone, String province) {
    this.id = id;
    this.name = name;
  }

  //Getter methods
  public String getId() {
    return this.id;
  }
  public String getName() {
    return this.name;
  }
}
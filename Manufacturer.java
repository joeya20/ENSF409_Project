// package edu.ucalgary.ensf409;

//Manufacturing class
public class Manufacturer {
  //Class fields
  private String id;
  private String name;
  private String phone;
  private String province;

  //Class constructor
  public Manufacturer (String id, String name, String phone, String province) {
    this.id = id;
    this.name = name;
    this.phone = phone;
    this.province = province;
  }

  //Getter methods
  public String getId() {
    return this.id;
  }
  public String getName() {
    return this.name;
  }
  public String getPhone() {
    return this.phone;
  }
  public String getProvince() {
    return this.province;
  }
}
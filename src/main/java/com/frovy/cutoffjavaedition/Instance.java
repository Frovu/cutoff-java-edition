package com.frovy.cutoffjavaedition;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "instances")
public class Instance {
  @Id
  private String id = UUID.randomUUID().toString();

  private Integer owner;
  private String name;
  private java.sql.Timestamp created;
  private java.sql.Timestamp completed;
  private java.sql.Timestamp datetime;

  private String model;
  private Float kp;
  private Float alt;
  private Float lat;
  private Float lon;
  private Float vertical;
  private Float azimutal;
  private Float lower;
  private Float upper;
  private Float step;
  private Float flightTime;

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }


  public Integer getOwner() {
    return owner;
  }
  public void setOwner(Integer owner) {
    this.owner = owner;
  }


  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }


  public java.sql.Timestamp getCreated() {
    return created;
  }
  public void setCreated(java.sql.Timestamp created) {
    this.created = created;
  }


  public java.sql.Timestamp getCompleted() {
    return completed;
  }
  public void setCompleted(java.sql.Timestamp completed) {
    this.completed = completed;
  }


  public java.sql.Timestamp getDatetime() {
    return datetime;
  }
  public void setDatetime(java.sql.Timestamp datetime) {
    this.datetime = datetime;
  }



  public String getModel() {
    return model;
  }
  public void setModel(String model) {
    this.model = model;
  }


  public Float getKp() {
    return kp;
  }
  public void setKp(Float kp) {
    this.kp = kp;
  }


  public Float getAlt() {
    return alt;
  }
  public void setAlt(Float alt) {
    this.alt = alt;
  }


  public Float getLon() {
    return lon;
  }
  public void setLon(Float lon) {
    this.lon = lon;
  }

  public Float getLat() {
    return lat;
  }
  public void setLat(Float lat) {
    this.lat = lat;
  }

  public Float getVertical() {
    return vertical;
  }
  public void setVertical(Float vertical) {
    this.vertical = vertical;
  }


  public Float getAzimutal() {
    return azimutal;
  }
  public void setAzimutal(Float azimutal) {
    this.azimutal = azimutal;
  }


  public Float getLower() {
    return lower;
  }
  public void setLower(Float lower) {
    this.lower = lower;
  }


  public Float getUpper() {
    return upper;
  }
  public void setUpper(Float upper) {
    this.upper = upper;
  }


  public Float getStep() {
    return step;
  }
  public void setStep(Float step) {
    this.step = step;
  }


  public Float getFlightTime() {
    return flightTime;
  }
  public void setFlightTime(Float flightTime) {
    this.flightTime = flightTime;
  }
}

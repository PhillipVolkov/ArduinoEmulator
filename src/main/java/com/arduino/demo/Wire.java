package com.arduino.demo;
 
import javax.persistence.*;
 
@Entity
@Table(name = "wires")
public class Wire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer wire_id;
 
    private Integer arduino_pin;
    private Integer component_pin;
    private Double wire_width;
    private String wire_color;
    
    public int getID() {
    	return wire_id;
    }
    
    public int getArduinoPin() {
    	return arduino_pin;
    }
    
    public int getComponentPin() {
    	return component_pin;
    }
    
    public double getWireWidth() {
    	return wire_width;
    }
    
    public String getWireColor() {
    	return wire_color;
    }
    
    @Override
    public String toString() {
    	return wire_width + " " + wire_color;
    }
}
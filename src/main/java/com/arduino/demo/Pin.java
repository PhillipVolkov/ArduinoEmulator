package com.arduino.demo;

import javax.persistence.*;
 
@Entity
@Table(name = "pins")
public class Pin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pin_id;
    
    private Integer component_id;
    @Enumerated(EnumType.STRING)
    private PinTypes pin_type;
    private String pin_name;
    private Double pin_voltage;
    private Integer pin_x;
    private Integer pin_y;
    private Integer pin_width;
    private Integer pin_height;
    
    public enum PinTypes {
    	power,
    	ground,
    	data
    }
    
    public int getID() {
    	return pin_id;
    }
    
    public int getComponent() {
    	return component_id;
    }
    
    public PinTypes getType() {
    	return pin_type;
    }
    
    public String getName() {
    	return pin_name;
    }

    public double getVoltage() {
    	return pin_voltage;
    }
    
    public int getX() {
    	return pin_x;
    }
    
    public int getY() {
    	return pin_y;
    }
    
    public int getWidth() {
    	return pin_width;
    }
    
    public int getHeight() {
    	return pin_height;
    }
}
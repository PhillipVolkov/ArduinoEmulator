package com.arduino.demo;
 
import javax.persistence.*;
 
@Entity
@Table(name = "components")
public class Component {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer component_id;
 
    private String component_name;
    private Integer component_x;
    private Integer component_y;
    private Integer user_id;
    
    public int getID() {
    	return component_id;
    }
    
    public String getName() {
    	return component_name;
    }
    
    public int getX() {
    	return component_x;
    }
    
    public int getY() {
    	return component_y;
    }
    
    @Override
    public String toString() {
    	return component_name + " [" + component_x + " , " + component_y + "], user:"+user_id;
    }
}
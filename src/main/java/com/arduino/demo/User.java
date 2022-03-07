package com.arduino.demo;
 
import javax.persistence.*;
 
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;
 
    private String user_name;
    private String user_password;
    
    public int getID() {
    	return user_id;
    }
}
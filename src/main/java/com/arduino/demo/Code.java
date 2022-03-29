package com.arduino.demo;
 
import javax.persistence.*;
 
@Entity
@Table(name = "code")
public class Code {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer code_id;

    private Integer user_id;
    private String code_body;
    
    public int getID() {
    	return code_id;
    }
    
    public int getUser() {
    	return user_id;
    }
    
    public String getCode() {
    	return code_body;
    }
}
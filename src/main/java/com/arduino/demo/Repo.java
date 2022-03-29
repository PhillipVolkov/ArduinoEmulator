package com.arduino.demo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.arduino.demo.Pin.PinTypes;

@Repository
public class Repo {
	@Autowired
    @PersistenceContext
    public EntityManager entityManager;
    
	//
	//USERS
	//
	User getUser(String username, String password) {
    	return entityManager.createQuery("select u from User u where u.user_name = ?1 and u.user_password = ?2", User.class)
	        .setParameter(1, username)
	        .setParameter(2, password)
    		.getSingleResult();
    }
	
    @Transactional
    public void insertUser(String username, String password) {
    	entityManager.createNativeQuery("insert into users (user_name, user_password) values (?,?)")
        .setParameter(1, username)
        .setParameter(2, password)
        .executeUpdate();
    }
    
    int getUserID(String username) {
    	return entityManager.createQuery("select u from User u where u.user_name = ?1", User.class)
    	     .setParameter(1, username)
    		.getSingleResult().getID();
    }
    
    //
    //COMPONENTS
    //
    @Transactional
    public void insertArduino(int user_id) {
    	entityManager.createNativeQuery("insert into components (component_name, component_x, component_y, component_scale, user_id) values ('arduino',0,0,0.5,?)")
        .setParameter(1, user_id)
        .executeUpdate();
    	
    	Integer arduinoID = getArduino(user_id).getID();
    	
    	String[] names = new String[] {"", "", "AREF", "GND1", "13", "12", "11", "10", "9", "8"};
		for (int i = 0; i < names.length; i++) {
			PinTypes type = PinTypes.data;
			if (names[i].length() >= 4 && names[i].substring(0, 3).equals("GND")) type = PinTypes.ground;
			
			entityManager.createNativeQuery("insert into pins (component_id, pin_type, pin_name, pin_voltage, pin_x, pin_y, pin_width, pin_height) values (?,cast(? as pin_types),?,?,?,?,?,?)")
	        .setParameter(1, arduinoID)
	        .setParameter(2, type.toString())
	        .setParameter(3, names[i])
	        .setParameter(4, 0)
	        .setParameter(5, (333+32.5*i))
	        .setParameter(6, 36)
	        .setParameter(7, 31.5)
	        .setParameter(8, 31.5)
	        .executeUpdate();
		}
		
		//draw P7-P0
		names = new String[] {"7", "6", "5", "4", "3", "2", "1", "0"};
		for (int i = 0; i < names.length; i++) {
			entityManager.createNativeQuery("insert into pins (component_id, pin_type, pin_name, pin_voltage, pin_x, pin_y, pin_width, pin_height) values (?,cast(? as pin_types),?,?,?,?,?,?)")
	        .setParameter(1, arduinoID)
	        .setParameter(2, PinTypes.data.toString())
	        .setParameter(3, names[i])
	        .setParameter(4, 0)
	        .setParameter(5, (677+32.5*i))
	        .setParameter(6, 36)
	        .setParameter(7, 31.5)
	        .setParameter(8, 31.5)
	        .executeUpdate();
		}
		
		//draw IOREF-VIN
		names = new String[] {"", "IOREF", "RESET", "3V3", "5V", "GND2", "GND3", "VIN"};
		for (int i = 0; i < names.length; i++) {
			double voltage = 0;
			PinTypes type = PinTypes.data;
			if (names[i].length() >= 4 && names[i].substring(0, 3).equals("GND")) type = PinTypes.ground;
			else if (names[i].equals("5V")) {
				type = PinTypes.power;
				voltage = 5;
			}
			else if (names[i].equals("3V3")) {
				type = PinTypes.power;
				voltage = 3.3;
			}
			
			entityManager.createNativeQuery("insert into pins (component_id, pin_type, pin_name, pin_voltage, pin_x, pin_y, pin_width, pin_height) values (?,cast(? as pin_types),?,?,?,?,?,?)")
	        .setParameter(1, arduinoID)
	        .setParameter(2, type.toString())
	        .setParameter(3, names[i])
	        .setParameter(4, voltage)
	        .setParameter(5, (450+32.5*i))
	        .setParameter(6, 653)
	        .setParameter(7, 31.5)
	        .setParameter(8, 31.5)
	        .executeUpdate();
		}
		
		//draw A0-A5
		names = new String[] {"A0", "A1", "A2", "A3", "A4", "A5"};
		for (int i = 0; i < names.length; i++) {
			entityManager.createNativeQuery("insert into pins (component_id, pin_type, pin_name, pin_voltage, pin_x, pin_y, pin_width, pin_height) values (?,cast(? as pin_types),?,?,?,?,?,?)")
	        .setParameter(1, arduinoID)
	        .setParameter(2, PinTypes.data.toString())
	        .setParameter(3, names[i])
	        .setParameter(4, 0)
	        .setParameter(5, (742+32.5*i))
	        .setParameter(6, 653)
	        .setParameter(7, 31.5)
	        .setParameter(8, 31.5)
	        .executeUpdate();
		}
    }
    
    @Transactional
    public void insertComponent(int user_id, String componentName) {
    	entityManager.createNativeQuery("insert into components (component_name, component_x, component_y, component_scale, user_id) values (?,0,0,0.5,?)")
        .setParameter(1, componentName)
        .setParameter(2, user_id)
        .executeUpdate();

    	List<Component> components = getComponents(user_id);
    	int componentID = components.get(components.size()-1).getID();
    	
    	if (componentName.equals("whiteLED")) {
    		entityManager.createNativeQuery("insert into pins (component_id, pin_type, pin_name, pin_voltage, pin_x, pin_y, pin_width, pin_height) values (?,cast(? as pin_types),?,?,?,?,?,?)")
	        .setParameter(1, componentID)
	        .setParameter(2, PinTypes.power.toString())
	        .setParameter(3, "POWER")
	        .setParameter(4, 0)
	        .setParameter(5, 13)
	        .setParameter(6, 425)
	        .setParameter(7, 30)
	        .setParameter(8, 30)
	        .executeUpdate();
			
    		entityManager.createNativeQuery("insert into pins (component_id, pin_type, pin_name, pin_voltage, pin_x, pin_y, pin_width, pin_height) values (?,cast(? as pin_types),?,?,?,?,?,?)")
	        .setParameter(1, componentID)
	        .setParameter(2, PinTypes.ground.toString())
	        .setParameter(3, "GND")
	        .setParameter(4, 0)
	        .setParameter(5, 59)
	        .setParameter(6, 405)
	        .setParameter(7, 30)
	        .setParameter(8, 30)
	        .executeUpdate();
    	}
    }
    
    Component getArduino(int user_id) {
    	return entityManager.createQuery("select c from Component c where c.user_id = ?1 and c.component_name = 'arduino'", Component.class)
    	        .setParameter(1, user_id)
        		.getSingleResult();
    }
    
    List<Component> getComponents(int user_id) {
    	return entityManager.createQuery("select c from Component c where c.user_id = ?1 and c.component_name != 'arduino'", Component.class)
    	        .setParameter(1, user_id)
        		.getResultList();
    }
    
    @Transactional
    public void updateComponent(int component_id, int component_x, int component_y) {
    	entityManager.createNativeQuery("update components set component_x = ?1, component_y = ?2 where component_id = ?3")
        .setParameter(1, component_x)
        .setParameter(2, component_y)
        .setParameter(3, component_id)
        .executeUpdate();
    }
    
    @Transactional
    public void deleteComponent(int component_id) {
    	entityManager.createNativeQuery("delete from components where component_id = ?")
        .setParameter(1, component_id)
        .executeUpdate();
    }
    
    //
    //PINS
    //
    List<Pin> getPins(int component) {
    	return entityManager.createQuery("select p from Pin p where p.component_id = ?1", Pin.class)
    	        .setParameter(1, component)
        		.getResultList();
    }
    
    @Transactional
    public void deletePins(int component_id) {
    	entityManager.createNativeQuery("delete from wires where (arduino_pin in (select pin_id from pins where component_id = ?)) or (component_pin in (select pin_id from pins where component_id = ?))")
        .setParameter(1, component_id)
        .setParameter(2, component_id)
        .executeUpdate();
    	
    	entityManager.createNativeQuery("delete from pins where component_id = ?")
        .setParameter(1, component_id)
        .executeUpdate();
    }
    
    
    //
    //WIRES
    //
    List<Wire> getWires(int arduino_id) {
    	return entityManager.createQuery("select w from Wire w where w.arduino_pin in (select p.pin_id from Pin p where component_id = ?1)", Wire.class)
        .setParameter(1, arduino_id)
		.getResultList();
    }
    
    List<Wire> getWires(int arduino_pin, int component_pin) {
    	return entityManager.createQuery("select w from Wire w where w.arduino_pin = ?1 and w.component_pin = ?2", Wire.class)
        .setParameter(1, arduino_pin)
        .setParameter(2, component_pin)
		.getResultList();
    }
    
    @Transactional
    public void insertWire(int arduino_pin, int component_pin, double width, String color) {
    	entityManager.createNativeQuery("insert into wires (arduino_pin, component_pin, wire_width, wire_color) values (?,?,?,?)")
        .setParameter(1, arduino_pin)
        .setParameter(2, component_pin)
        .setParameter(3, width)
        .setParameter(4, color)
        .executeUpdate();
    }
    
    @Transactional
    public void deleteWire(int component_pin) {
    	entityManager.createNativeQuery("delete from wires where (arduino_pin = ?) or (component_pin = ?)")
        .setParameter(1, component_pin)
        .setParameter(2, component_pin)
        .executeUpdate();
    }
    
    //
    //CODE
    //
    Code getCode(int user_id) {
    	return entityManager.createQuery("select c from Code c where c.user_id = ?1", Code.class)
        .setParameter(1, user_id)
		.getSingleResult();
    }
    
    @Transactional
    public void insertCode(int user_id, String code) {
    	entityManager.createNativeQuery("insert into code (user_id, code_body) values (?,?)")
        .setParameter(1, user_id)
        .setParameter(2, code)
        .executeUpdate();
    }
    
    @Transactional
    public void updateCode(int user_id, String code) {
    	entityManager.createNativeQuery("update code set code_body = ?1")
        .setParameter(1, code)
        .executeUpdate();
    }
}
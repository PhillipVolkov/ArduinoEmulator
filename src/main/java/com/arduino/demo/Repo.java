package com.arduino.demo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class Repo {
	@Autowired
    @PersistenceContext
    public EntityManager entityManager;
    
	//insert merchant into POSTGRES
    @Transactional
    public void insertUser(String username, String password) {
    	entityManager.createNativeQuery("insert into users (user_name, user_password) values (?,?)")
        .setParameter(1, username)
        .setParameter(2, password)
        .executeUpdate();
    }
    
    @Transactional
    public void insertArduino(int user_id) {
    	entityManager.createNativeQuery("insert into components (component_name, component_x, component_y, user_id) values ('arduino',0,0,?)")
        .setParameter(1, user_id)
        .executeUpdate();
    }
    
    @Transactional
    public void updateComponent(int component_id, int component_x, int component_y) {
    	entityManager.createNativeQuery("update components set component_x = ?1, component_y = ?2 where component_id = ?3")
        .setParameter(1, component_x)
        .setParameter(2, component_y)
        .setParameter(3, component_id)
        .executeUpdate();
    }
    
    User getUser(String username, String password) {
    	return entityManager.createQuery("select u from User u where u.user_name = ?1 and u.user_password = ?2", User.class)
	        .setParameter(1, username)
	        .setParameter(2, password)
    		.getSingleResult();
    }
    
    int getUserID(String username) {
    	return entityManager.createQuery("select u from User u where u.user_name = ?1", User.class)
    	     .setParameter(1, username)
    		.getSingleResult().getID();
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
}
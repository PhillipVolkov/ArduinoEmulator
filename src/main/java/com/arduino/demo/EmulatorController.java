package com.arduino.demo;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class EmulatorController {
    @Autowired
	private Repo repo;
	
	@RequestMapping("/")
    public String main(@RequestParam(name = "newUser", required=false) Boolean newUser, HttpSession session, Model model) {
		if (newUser == null) newUser = false;
		
		String currUser = null;
		Component arduino = null;
		List<Component> components = null;
		if (session.getAttribute("username") != null) {
			currUser = session.getAttribute("username").toString();
			Integer userID = null;
			try {
				userID = repo.getUserID(currUser);
			}
			catch(Exception e) {
				session.removeAttribute("username");
				currUser = null;
			}
			
			if (currUser != null) {
				try {
					arduino = repo.getArduino(userID);
				}
				catch(Exception e) {
					repo.insertArduino(userID);
					arduino = repo.getArduino(userID);
				}
				
				components = repo.getComponents(userID);
			}
		}
		
		model.addAttribute("newUser", newUser);
		model.addAttribute("user", currUser);
		model.addAttribute("arduino", arduino);
		model.addAttribute("components", components);
        return "EmulatorPage";
    }
	
	@RequestMapping(value="/save", method = RequestMethod.GET)
	public RedirectView saveRedirect(Model model) {
		return new RedirectView("/");
    }
	
	@RequestMapping(value="/save", method = RequestMethod.POST)
	public RedirectView submit(@RequestParam(name = "components") List<String> components,
			HttpSession session, Model model) {
		
		Integer[] componentVals = new Integer[3];
		int i = 0;
		for (String val : components) {
			if (val.equals("|")) {
				System.out.println(Arrays.toString(componentVals));
				repo.updateComponent(componentVals[0], componentVals[1], componentVals[2]);
				componentVals = new Integer[3];
			}
			else {
				componentVals[i] = (int)Math.round(Double.parseDouble(val));
				i++;
			}
		}
		
		return new RedirectView("/");
	}

	@RequestMapping(value="/user", method = RequestMethod.GET)
	public RedirectView userRedirect(Model model) {
		return new RedirectView("/");
    }
	
	@RequestMapping(value="/user", method = RequestMethod.POST)
	public String submit(@RequestParam(name = "newUser") boolean newUser, @RequestParam(name = "username") String username, 
			@RequestParam(name = "password") String password, @RequestParam(name = "passwordConfirm", required=false) String confirmPassword,  
			HttpSession session, Model model) {
		String errorMessage = "";
		
		if (newUser) {
			errorMessage = "Account successfully created!";
			
			if (!password.equals(confirmPassword)) {
				errorMessage = "Passwords do not match!";
			}
			else {
				//check if user already exists
				try {
					repo.insertUser(username, password);
					newUser = false;
				}
				catch (Exception e) {
					errorMessage = "User already exists!";
				}
			}
		}
		else {
			try {
				repo.getUser(username, password);
				session.setAttribute("username", username);
			}
			catch(Exception e) {
				errorMessage = "Incorrect credentials!";
			}
		}

		String currUser = null;
		if (session.getAttribute("username") != null) currUser = session.getAttribute("username").toString();
		
		model.addAttribute("errorMessage", errorMessage);
		model.addAttribute("newUser", newUser);
		model.addAttribute("user", currUser);
		return "EmulatorPage";
	}
	
	@RequestMapping("/logout")
	public RedirectView logout(HttpSession session, Model model) {
		session.removeAttribute("username");
		return new RedirectView("/");
    }
}
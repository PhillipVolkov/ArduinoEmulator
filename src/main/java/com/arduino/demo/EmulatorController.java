package com.arduino.demo;

import java.util.ArrayList;
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

import com.arduino.demo.ParsedCodeFolder.CodeParser;

@Controller
public class EmulatorController {
    @Autowired
	private Repo repo;
	
	@RequestMapping("/")
    public String main(@RequestParam(name = "newUser", required=false) Boolean newUser, HttpSession session, Model model) {
		if (newUser == null) newUser = false;
		
		String currUser = null;
		Component arduino = null;
		List<Pin> arduinoPins = null;
		
		List<Component> components = null;
		ArrayList<List<Pin>> pins = new ArrayList<>();
		List<Wire> wires = null;
		
		Code code = null;
		CodeParser parsedCode = null;
		
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
				arduinoPins = repo.getPins(arduino.getID());
				
				components = repo.getComponents(userID);
				for (Component c : components) {
					pins.add(repo.getPins(c.getID()));
				}
				wires = repo.getWires(arduino.getID());
				
				try {
					code = repo.getCode(userID);
				}
				catch(Exception e) {
					repo.insertCode(userID, "");
					code = repo.getCode(userID);
				}
				
				parsedCode = new CodeParser(code);
			}
		}
		
		model.addAttribute("newUser", newUser);
		model.addAttribute("user", currUser);
		model.addAttribute("arduino", arduino);
		model.addAttribute("components", components);
		model.addAttribute("arduinoPins", arduinoPins);
		model.addAttribute("pins", pins);
		model.addAttribute("wires", wires);
		model.addAttribute("code", code);
		if(parsedCode != null) model.addAttribute("parsedCode", parsedCode.getStructures());
        return "EmulatorPage";
    }
	
	@RequestMapping(value="/saveCode", method = RequestMethod.GET)
	public RedirectView saveCodeRedirect(Model model) {
		return new RedirectView("/");
    }
	
	@RequestMapping(value="/saveCode", method = RequestMethod.POST)
	public RedirectView saveCode(@RequestParam(name = "code") String code, HttpSession session, Model model) {
		if (session.getAttribute("username") != null) {
			String currUser = session.getAttribute("username").toString();
			Integer userID = null;
			try {
				userID = repo.getUserID(currUser);
			}
			catch(Exception e) {
				session.removeAttribute("username");
				currUser = null;
			}
			
			repo.updateCode(userID, code);
		}
		
		return new RedirectView("/");
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

	@RequestMapping(value="/addWire", method = RequestMethod.GET)
	public RedirectView wireRedirect(Model model) {
		return new RedirectView("/");
    }
	
	@RequestMapping(value="/addWire", method = RequestMethod.POST)
	public RedirectView addWire(@RequestParam(name = "pins") List<Integer> pins, @RequestParam(name = "width") double width, @RequestParam(name = "color") String color, 
			HttpSession session, Model model) {
		if (session.getAttribute("username") != null) {
			String currUser = session.getAttribute("username").toString();
			Integer userID = null;
			try {
				userID = repo.getUserID(currUser);
			}
			catch(Exception e) {
				session.removeAttribute("username");
				currUser = null;
			}
			
			width = Math.round(Math.pow(10, 6)*width)/Math.pow(10, 6);
			
			//only add wire if there is not one there currently
			if (repo.getWires(pins.get(0), pins.get(1)).size() == 0) {
				repo.insertWire(pins.get(0), pins.get(1), width, color);
			}
		}
		
		return new RedirectView("/");
	}
	
	@RequestMapping(value="/removeWire", method = RequestMethod.GET)
	public RedirectView wireRemoveRedirect(Model model) {
		return new RedirectView("/");
    }
	
	@RequestMapping(value="/removeWire", method = RequestMethod.POST)
	public RedirectView removeWire(@RequestParam(name = "pin") Integer pin, HttpSession session, Model model) {
		if (session.getAttribute("username") != null) {
			String currUser = session.getAttribute("username").toString();
			Integer userID = null;
			try {
				userID = repo.getUserID(currUser);
			}
			catch(Exception e) {
				session.removeAttribute("username");
				currUser = null;
			}
			
			//only remove wire if there is there is one currently
			repo.deleteWire(pin);
		}
		
		return new RedirectView("/");
	}
	
	@RequestMapping(value="/addComponent", method = RequestMethod.GET)
	public RedirectView addRedirect(Model model) {
		return new RedirectView("/");
    }
	
	@RequestMapping(value="/addComponent", method = RequestMethod.POST)
	public RedirectView addComponent(@RequestParam(name = "newComponent") String componentName, HttpSession session, Model model) {
		if (session.getAttribute("username") != null) {
			String currUser = session.getAttribute("username").toString();
			Integer userID = null;
			try {
				userID = repo.getUserID(currUser);
			}
			catch(Exception e) {
				session.removeAttribute("username");
				currUser = null;
			}
			repo.insertComponent(userID, componentName);
		}
		
		return new RedirectView("/");
	}
	
	@RequestMapping(value="/deleteComponent", method = RequestMethod.GET)
	public RedirectView deleteRedirect(Model model) {
		return new RedirectView("/");
    }
	
	@RequestMapping(value="/deleteComponent", method = RequestMethod.POST)
	public RedirectView deleteComponent(@RequestParam(name = "deleteComponent") Integer componentID, HttpSession session, Model model) {
		if (session.getAttribute("username") != null) {
			String currUser = session.getAttribute("username").toString();
			Integer userID = null;
			try {
				userID = repo.getUserID(currUser);
			}
			catch(Exception e) {
				session.removeAttribute("username");
				currUser = null;
			}
			repo.deletePins(componentID);
			repo.deleteComponent(componentID);
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
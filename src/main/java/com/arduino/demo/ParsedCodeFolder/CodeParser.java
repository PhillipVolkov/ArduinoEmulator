package com.arduino.demo.ParsedCodeFolder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

import com.arduino.demo.Code;
import com.arduino.demo.ParsedCodeFolder.Conditional.ComparisonType;
import com.arduino.demo.ParsedCodeFolder.Conditional.ConditionalType;
import com.arduino.demo.ParsedCodeFolder.Conditional.Operand;

public class CodeParser {
	private Code codeOBJ;
	private String code;
	private Queue<CodeLine> codeLines = new LinkedList<>();
	///private ArrayList<int[]> errors = new ArrayList<>();
	
	public CodeParser(Code codeOBJ) {
		this.codeOBJ = codeOBJ;
		this.code = codeOBJ.getCode();

		code = code.replaceAll("<div>", "");
		code = code.replaceAll("</div>", "");
		code = code.replaceAll("<br>", "");
		
		parseCode(code);
	}
	
	public Queue<CodeLine> getStructures() {
		return codeLines;
	}
	
	//method to parse multiple lines of code
	private void parseCode(String code) {
		Scanner lineParser = new Scanner(code).useDelimiter(Pattern.compile(";"));
		Stack<Structure> structures = new Stack<>();
		
		//iterate through each line of code
		while (lineParser.hasNext()) {
			String next = lineParser.next();
			
			//detect end of structure and add it to the main queue
			while (next.contains("}")) {
				try {
					if (structures.size() == 1) codeLines.add(structures.pop());
					else {
						Structure curr = structures.pop();
						structures.peek().addCommand(curr);
					}
				}
				catch(Exception e) {}
				
				next = next.substring(next.indexOf("}")+1);
			}
			//detect start of structure and add it to the stack
			while (next.contains("{")) {
				structures.add(new Structure(parseConditional(next)));
				next = next.substring(next.indexOf("{")+1);
			}
			
			//find the top-most structure on the stack
			Structure currStructure = null;
			if (structures.size() != 0) currStructure = structures.peek();
			
			//parse each command of code
			parseLine(currStructure, next);
		}
	}
	
	private Conditional parseConditional(String line) {
		line = line.substring(line.indexOf("}")+1, line.indexOf("{"));
		line = line.strip();
		
		ConditionalType type = null;
		Object value1 = null;
		ComparisonType value1Type = null;
		Object value2 = null;
		ComparisonType value2Type = null;
		Operand operand = null;
		
		String conditional = "if";
		if (line.length() >= conditional.length() && line.substring(0, conditional.length()).equals(conditional)) {
			type = ConditionalType.If;
			
			line = line.substring(conditional.length()+1);
			line.strip();
			
			line = line.substring(1, line.length()-1);
			
			if (line.contains("==")) {
				operand = Operand.Equals;
				
				line = line.replace(" ", "");
				String[] parameters = line.split("==");

				Object[] returns = parseConditionalValue(parameters[0]);
				value1 = returns[0];
				value1Type = (ComparisonType)returns[1];

				returns = parseConditionalValue(parameters[1]);
				value2 = returns[0];
				value2Type = (ComparisonType)returns[1];
			}
		}
		
		conditional = "else if";
		if (line.length() >= conditional.length() && line.substring(0, conditional.length()).equals(conditional)) {
			type = ConditionalType.ElseIf;
			
			line = line.substring(conditional.length()+1);
			line.strip();
			
			line = line.substring(1, line.length()-1);
			
			if (line.contains("==")) {
				operand = Operand.Equals;
				
				line = line.replace(" ", "");
				String[] parameters = line.split("==");

				Object[] returns = parseConditionalValue(parameters[0]);
				value1 = returns[0];
				value1Type = (ComparisonType)returns[1];

				returns = parseConditionalValue(parameters[1]);
				value2 = returns[0];
				value2Type = (ComparisonType)returns[1];
			}
		}
		
		conditional = "else";
		if (line.length() >= conditional.length() && line.substring(0, conditional.length()).equals(conditional)) {
			type = ConditionalType.Else;
		}
		
		conditional = "while";
		if (line.length() >= conditional.length() && line.substring(0, conditional.length()).equals(conditional)) {
			type = ConditionalType.While;
			
			line = line.substring(conditional.length()+1);
			line.strip();
			
			line = line.substring(1, line.length()-1);
			
			if (line.contains("==")) {
				operand = Operand.Equals;
				
				line = line.replace(" ", "");
				String[] parameters = line.split("==");

				Object[] returns = parseConditionalValue(parameters[0]);
				value1 = returns[0];
				value1Type = (ComparisonType)returns[1];

				returns = parseConditionalValue(parameters[1]);
				value2 = returns[0];
				value2Type = (ComparisonType)returns[1];
			}
		}
		
		return new Conditional(type, value1, value1Type, value2, value2Type, operand);
	}
	
	private Object[] parseConditionalValue(String parameter) {
		Object value = null;
		ComparisonType type = null;
		
		if (parameter.equals("HIGH")) {
			type = ComparisonType.Boolean;
			value = true;
		}
		else if (parameter.equals("LOW")) {
			type = ComparisonType.Boolean;
			value = false;
		}
		else if (parameter.equals("true")) {
			type = ComparisonType.Boolean;
			value = true;
		}
		else if (parameter.equals("false")) {
			type = ComparisonType.Boolean;
			value = false;
		}
		
		if (type == null) {
			PinReadCommand pinRead = parsePinRead(parameter);
			if (pinRead != null) {
				type = ComparisonType.Pin;
				value = pinRead.getTarget();
			}
		}

		return new Object[] {value, type};
	}
	
	
	private void parseLine(Structure structure, String line) {
		line = line.substring(line.indexOf("}")+1);
		line = line.substring(line.indexOf("{")+1);
		line = line.strip();
		
		if (line.equals("")) return;
		
		//logic for digitalWrite
		//check if method name matches "digitalWrite"
		String function = "digitalWrite";
		if (line.length() >= function.length() && line.substring(0, function.length()).equals(function)) {
			//then, parse the two parameters
			String[] parameters = line.substring(function.length()+1, line.length()-1).split(",");
			parameters[0] = parameters[0].strip();
			parameters[1] = parameters[1].strip();
			
			//check if the parameters are valid
			if (parameters[1].equals("HIGH") || parameters[1].equals("LOW")) {
				try {
					boolean setState = false;
					if (parameters[1].equals("HIGH")) setState = true;
					
					int pinId = Integer.parseInt(parameters[0]);
					
					//then, create a new pinWrite command and append it
					Command pinWrite = new PinWriteCommand(pinId, setState);
					if (structure != null) structure.addCommand(pinWrite);
					else codeLines.add(pinWrite);
				}
				catch (Exception e) {}
			}
		}
		
		//logic for digital read
		//run parsePinRead method (similar logical to digitalWrite)
		Command pinRead = parsePinRead(line);
		if (pinRead != null) {
			if (structure != null) structure.addCommand(pinRead);
			else codeLines.add(pinRead);
		}

		//logic for delay
		//check if method name matches "delay"
		function = "delay";
		if (line.length() >= function.length() && line.substring(0, function.length()).equals(function)) {
			//check if parameter is a valid integer
			try {
				int time = Integer.parseInt(line.substring(function.length()+1, line.length()-1));
				
				//then, create a new delay command and append it
				Command delay = new DelayCommand(time);
				if (structure != null) structure.addCommand(delay);
				else codeLines.add(delay);
			}
			catch(Exception e) {}
		}
	}
	
	private PinReadCommand parsePinRead(String line) {
		String function = "digitalRead";
		if (line.length() >= function.length() && line.substring(0, function.length()).equals(function)) {
			String pinParameter = line.substring(function.length()+1, line.length()-1);
			pinParameter = pinParameter.strip();
			
			try {
				int pinId = Integer.parseInt(pinParameter);
				
				return new PinReadCommand(pinId);
			}
			catch (Exception e) {}
		}
		
		return null;
	}
}

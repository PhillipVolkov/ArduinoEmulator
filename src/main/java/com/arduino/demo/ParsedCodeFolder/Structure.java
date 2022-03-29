package com.arduino.demo.ParsedCodeFolder;

import java.util.LinkedList;
import java.util.Queue;

public class Structure implements CodeLine {
	private Conditional conditional;
	private Queue<CodeLine> commands;
	
	public Structure(Conditional conditional) {
		this.conditional = conditional;
		commands = new LinkedList<CodeLine>();
	}
	
	@Override
	public boolean isStructure() {
		return true;
	}
	
	public Conditional getConditional() {
		return conditional;
	}
	
	public void resetCommands() {
		commands = new LinkedList<CodeLine>();
	}
	
	public void addCommand(CodeLine command) {
		this.commands.add(command);
	}
	
	public Queue<CodeLine> getCommands() {
		return commands;
	}
	
	@Override
	public String toString() {
		return conditional.getType() + " " + conditional.getValue1() + " " + conditional.getOperand() + " " + conditional.getValue2();
	}
}
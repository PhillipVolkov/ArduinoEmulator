package com.arduino.demo.ParsedCodeFolder;

public interface Command extends CodeLine {
	public enum CommandType {
		Write,
		Read,
		Delay
	}
	
	public CommandType getType();
}
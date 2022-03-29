package com.arduino.demo.ParsedCodeFolder;

public class PinReadCommand implements Command {
	private int targetPin;
	
	public PinReadCommand(int targetPin) {
		this.targetPin = targetPin;
	}
	
	@Override
	public boolean isStructure() {
		return false;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.Read;
	}
	
	public int getTarget() {
		return targetPin;
	}
}
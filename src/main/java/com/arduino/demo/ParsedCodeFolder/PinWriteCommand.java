package com.arduino.demo.ParsedCodeFolder;

public class PinWriteCommand implements Command {
	private int targetPin;
	private boolean setState;
	
	public PinWriteCommand(int targetPin, boolean setState) {
		this.targetPin = targetPin;
		this.setState = setState;
	}
	
	@Override
	public boolean isStructure() {
		return false;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.Write;
	}
	
	public int getTarget() {
		return targetPin;
	}
	
	public boolean getSetState() {
		return setState;
	}
}
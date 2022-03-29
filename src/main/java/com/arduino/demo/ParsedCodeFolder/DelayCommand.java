package com.arduino.demo.ParsedCodeFolder;

public class DelayCommand implements Command {
	private double time;
	
	public DelayCommand(double time) {
		this.time = time;
	}
	
	@Override
	public boolean isStructure() {
		return false;
	}
	
	@Override
	public CommandType getType() {
		return CommandType.Delay;
	}
	
	public double getTime() {
		return time;
	}
}

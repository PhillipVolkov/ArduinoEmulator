package com.arduino.demo.ParsedCodeFolder;

public class Conditional {
	ConditionalType type;
	Object value1;
	ComparisonType value1Type;
	Object value2;
	ComparisonType value2Type;
	Operand operand;
	
	public enum ComparisonType {
		Boolean,
		Integer,
		Pin,
		Variable
	}
	
	public enum Operand {
		Equals,
		NotEquals,
		Less,
		LessEquals,
		Greater,
		GreaterEquals
	}
	
	public enum ConditionalType {
		If,
		ElseIf,
		Else,
		While
	}
	
	public Conditional(ConditionalType type, Object value1, ComparisonType value1Type, Object value2, ComparisonType value2Type, Operand operand) {
		this.type = type;
		this.value1 = value1;
		this.value1Type = value1Type;
		this.value2 = value2;
		this.value2Type = value2Type;
		this.operand = operand;
	}
	
	public ConditionalType getType() {
		return type;
	}
	
	public Object getValue1() {
		return value1;
	}
	
	public ComparisonType getValue1Type() {
		return value1Type;
	}
	
	public Object getValue2() {
		return value2;
	}
	
	public ComparisonType getValue2Type() {
		return value2Type;
	}
	
	public Operand getOperand() {
		return operand;
	}
	
	@Override
	public String toString() {
		return getValue1() + " " + getOperand() + " " + getValue2();
	}
}

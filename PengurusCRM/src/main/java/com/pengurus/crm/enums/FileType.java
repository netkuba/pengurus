package com.pengurus.crm.enums;

public enum FileType {

	input (1),
	output (2);
	
	private final long type;

	FileType(long type) {
		this.type = type;
	}

	public long type() {
		return type;
	}
}
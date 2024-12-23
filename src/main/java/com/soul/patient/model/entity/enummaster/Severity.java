package com.soul.patient.model.entity.enummaster;

import lombok.Getter;

@Getter
public enum Severity
{
	LOW("SEVERITY1", 634),
	MEDIUM("SEVERITY2", 635),
	HIGH("SEVERITY3", 636);
	
	private final String lookupCode;
	private final int lookupId;
	
	Severity(String lookupCode, int lookupId) {
		this.lookupCode = lookupCode;
		this.lookupId = lookupId;
	}
	
	
}

package com.soul.patient.auth;

import lombok.Data;
import org.springframework.stereotype.Service;

@Service("authAbstract")
@Data
public class AuthAbstract
{

	private String bearerToken = null;

}

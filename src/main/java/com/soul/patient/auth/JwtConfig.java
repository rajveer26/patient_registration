package com.soul.patient.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service("jwtConfig")
public class JwtConfig
{
	
	@Value("${spring.rsocket.server.ssl.certificate}")
	private String keyPath;
	
	public PublicKey generateJwtKeyDecryption() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException{
		
		try{
			ClassPathResource resource = new ClassPathResource(new URI(keyPath.concat("public_key.pem")).toString());
			
			String publicKeyPEM = new String(Files.readAllBytes(resource.getFile().toPath())).replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replaceAll("\\s+", "");
			
			byte[] decodedKey = Base64.getDecoder().decode(publicKeyPEM);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			
			return keyFactory.generatePublic(keySpec);
		}
		catch(URISyntaxException e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}
	
}


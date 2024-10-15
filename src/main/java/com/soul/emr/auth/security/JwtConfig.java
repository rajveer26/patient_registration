package com.soul.emr.auth.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service("jwtConfig")
public class JwtConfig
{
	
	@Value("${spring.rsocket.server.ssl.certificate}")
	private String keyPath;
	
	
	//method to generateJwtKeyEncryption
	public PrivateKey generateJwtKeyEncryption() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException{
		
		try{
			//creating a new ClassPathResource object
			ClassPathResource resource = new ClassPathResource(new URI(keyPath.concat("private_key.pem")).toString());
			
			//storing all the bytes present in privateKey
			String privateKeyPEM = new String(Files.readAllBytes(resource.getFile().toPath())).replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replaceAll("\\s+", "");
			
			//using Base64Decoder to decode the string
			byte[] keyBytes = Base64.getDecoder().decode(privateKeyPEM);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			
			//returning private key
			return keyFactory.generatePrivate(keySpec);
		}
		//catch block
		catch(URISyntaxException e)
		{
			//throwing a runTimeException with a message
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public PublicKey generateJwtKeyDecryption() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException{
		
		try{
			ClassPathResource resource = new ClassPathResource(new URI(keyPath.concat("public_key.pem")).toString());
			
			String publicKeyPEM = new String(Files.readAllBytes(resource.getFile().toPath())).replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replaceAll("\\s+", "");
			
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


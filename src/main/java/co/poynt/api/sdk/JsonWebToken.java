package co.poynt.api.sdk;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.KeyPair;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class JsonWebToken {
	private KeyPair keyPair;
	private Config config;

	public JsonWebToken(Config config) {
		Security.addProvider(new BouncyCastleProvider());
		this.config = config;
	}

	public JsonWebToken init() throws IOException {
		Security.addProvider(new BouncyCastleProvider());
		Reader reader;
		String keyFile = config.getAppKeyFile();
		boolean isOnClasspath = keyFile.startsWith("classpath:");
		if (isOnClasspath) {
			keyFile = keyFile.substring(10);
			reader = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(keyFile));
		} else {
			reader = new FileReader(new File(keyFile));
		}
		try (PEMParser pp = new PEMParser(reader)) {
			PEMKeyPair pemKeyPair = (PEMKeyPair) pp.readObject();
			keyPair = new JcaPEMKeyConverter().getKeyPair(pemKeyPair);
		}
		return this;
	}

	public String selfIssue() {
		JWSSigner signer = new RSASSASigner((RSAPrivateKey) keyPair.getPrivate());

		List<String> aud = new ArrayList<String>();
		aud.add(config.getApiHost());

		JWTClaimsSet claimsSet = new JWTClaimsSet();
		claimsSet.setAudience(aud);
		claimsSet.setSubject(config.getAppId());
		claimsSet.setIssuer(config.getAppId());
		Calendar now = Calendar.getInstance();
		claimsSet.setIssueTime(now.getTime());
		now.add(Calendar.MINUTE, 15);
		claimsSet.setExpirationTime(now.getTime());
		claimsSet.setJWTID(UUID.randomUUID().toString());

		SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);

		try {
			signedJWT.sign(signer);
		} catch (JOSEException e) {
			throw new PoyntSdkException("Failed to sign self issued JWT.");
		}
		return signedJWT.serialize();
	}
}

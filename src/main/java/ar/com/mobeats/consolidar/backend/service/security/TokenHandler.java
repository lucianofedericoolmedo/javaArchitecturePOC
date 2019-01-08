package ar.com.mobeats.consolidar.backend.service.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import ar.com.mobeats.consolidar.backend.dto.UserData;
import ar.com.mobeats.consolidar.backend.service.exception.TokenGenerationException;
import ar.com.mobeats.consolidar.backend.service.exception.UnauthorizedException;
import ar.com.mobeats.consolidar.backend.util.DateUtil;
import ar.com.mobeats.consolidar.backend.util.JSONObjectConverter;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

/**
 * 
 * @author Adrian Paredes
 *
 */
@Service
public class TokenHandler {
    
    private String headerContentType;
    private String simetricKey;
    private int expirationInMinutes;
    private boolean expireAtMidnight;
    private String issuer;
    private String audience;
    private String subject;
    
    private static final String SCOPE_KEY = "scope";
    private static final String USER_KEY = "key";
    
    private static final long SECONDS_IN_A_MINUTE = 60;
    private static final long MILISECONDS_IN_A_SECOND = 1000;
    
    public UserData verifyToken(String token) {
        
        SignedJWT jwt = parseToken(token);
        
        verifySignature(jwt);
        
        ReadOnlyJWTClaimsSet claims = getClaims(jwt);

        verifyIssuer(claims);
        verifyAudience(claims);
        verifySubject(claims);

        verifyExpiration(claims);

        return getUser(claims);
    }
    
    public UserData getExpiredUser(String token) {
        
        SignedJWT jwt = parseToken(token);
        
        verifySignature(jwt);
        
        ReadOnlyJWTClaimsSet claims = getClaims(jwt);

        verifyIssuer(claims);
        verifyAudience(claims);
        verifySubject(claims);

        return getUser(claims);
    }

    public String createToken(UserData userData) {
        
        JWTClaimsSet jwtClaims = createJWTClaims();
        jwtClaims.setCustomClaim(SCOPE_KEY, "user");
        jwtClaims.setCustomClaim(USER_KEY, JSONObjectConverter.convertToJSON(userData));
        
        // Create JWS header with HS256 algorithm
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        header.setContentType(headerContentType);

        SignedJWT jwt = new SignedJWT(header, jwtClaims);

        // sign the JSON Web Token
        JWSSigner signer = new MACSigner(simetricKey.getBytes());
        
        try {
            jwt.sign(signer);
        } catch (Exception e) {
            throw new TokenGenerationException(e);
        }
        
        return jwt.serialize();
    }
    
    private Date calculateExpirationTime() {
        if (expireAtMidnight) {
            return DateUtil.proximoInicioDeDia();
        } else {
            return new Date(new Date().getTime() + MILISECONDS_IN_A_SECOND * SECONDS_IN_A_MINUTE * expirationInMinutes);
        }
    }
    
    private JWTClaimsSet createJWTClaims() {
        JWTClaimsSet jwtClaims = new JWTClaimsSet();
        jwtClaims.setIssuer(issuer);
        jwtClaims.setSubject(subject);
        
        List<String> aud = new ArrayList<String>();
        aud.add(audience);
        jwtClaims.setAudience(aud);
        
        jwtClaims.setExpirationTime(calculateExpirationTime());
        jwtClaims.setNotBeforeTime(new Date());
        jwtClaims.setIssueTime(new Date());
        jwtClaims.setJWTID(UUID.randomUUID().toString());
        
        return jwtClaims;
    }
    
    private SignedJWT parseToken(String token) {
        SignedJWT jwt = null;        
        try {
            jwt = SignedJWT.parse(token);
        } catch (Exception e) {
            throw new UnauthorizedException(e);
        }
        return jwt;
    }

    private void verifySignature(SignedJWT jwt) {
        JWSVerifier verifier = new MACVerifier(simetricKey);
        boolean verifiedSignature = false;
        try {
            verifiedSignature = jwt.verify(verifier);
        } catch (Exception e) {
            throw new UnauthorizedException(e);
        }
        if (!verifiedSignature) {
            throw new UnauthorizedException("verifySignature FAILED!");
        }
    }
    
    private ReadOnlyJWTClaimsSet getClaims(SignedJWT jwt) {
        ReadOnlyJWTClaimsSet claims = null;
        try {
            claims = jwt.getJWTClaimsSet();
        } catch (Exception e) {
            throw new UnauthorizedException(e);
        }
        return claims;
    }
    
    private void verifyExpiration(ReadOnlyJWTClaimsSet claims) {
        Date expiration = claims.getExpirationTime();
        Date now = new Date();
        if (now.getTime() >= expiration.getTime()) {
            throw new UnauthorizedException("verifyExpiration FAILED!");
        }
    }
    
    private void verifyIssuer(ReadOnlyJWTClaimsSet claims) {
        if (!issuer.equals(claims.getIssuer())) {
            throw new UnauthorizedException("verifyIssuer FAILED!");
        }
    }
    
    private void verifyAudience(ReadOnlyJWTClaimsSet claims) {
        List<String> au = claims.getAudience();
        if (au == null || au.size() != 1) {
            throw new UnauthorizedException("verifyAudience FAILED!");
        }
        if (!audience.equals(au.get(0))) {
            throw new UnauthorizedException("verifyAudience FAILED!");
        }
    }
    
    private void verifySubject(ReadOnlyJWTClaimsSet claims) {
        if (!subject.equals(claims.getSubject())) {
            throw new UnauthorizedException("verifySubject FAILED!");
        }
    }
    
    private UserData getUser(ReadOnlyJWTClaimsSet claims) {
        String userJSON = (String) claims.getCustomClaim(USER_KEY);
        if (StringUtils.isBlank(userJSON)) {
            throw new UnauthorizedException("USER not found in custom claim's token");
        }
        try {
            return JSONObjectConverter.convertToObject(userJSON, UserData.class);
        } catch (Exception e) {
            throw new UnauthorizedException(e);
        }
    }

    public String getHeaderContentType() {
        return headerContentType;
    }

    public void setHeaderContentType(String headerContentType) {
        this.headerContentType = headerContentType;
    }

    public String getSimetricKey() {
        return simetricKey;
    }

    public void setSimetricKey(String simetricKey) {
        this.simetricKey = simetricKey;
    }

    public int getExpirationInMinutes() {
        return expirationInMinutes;
    }

    public void setExpirationInMinutes(int expirationInMinutes) {
        this.expirationInMinutes = expirationInMinutes;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isExpireAtMidnight() {
        return expireAtMidnight;
    }

    public void setExpireAtMidnight(boolean expireAtMidnight) {
        this.expireAtMidnight = expireAtMidnight;
    }
    
}

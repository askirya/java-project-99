package hexlet.code.util;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Utility for JWT generation.
 */
@Component
public class JWTUtils {

    private final JwtEncoder encoder;

    /**
     * Creates JWT utility.
     * @param encoder JWT encoder
     */
    public JWTUtils(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    /**
     * Generates a JWT for the given username (email).
     * @param username user email
     * @return token value
     */
    public String generateToken(String username) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(username)
                .build();
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}

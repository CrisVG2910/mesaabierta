package cl.duoc.mesaabierta.service;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Service;
@Service
public class JwtService {
  private final JwtEncoder encoder;
  private final String issuer;
  public JwtService(JwtEncoder encoder,@Value("${app.jwt.issuer}") String issuer){this.encoder=encoder;this.issuer=issuer;}
  public String emitir(String username){
    Instant ahora=Instant.now();
    JwtClaimsSet claims=JwtClaimsSet.builder().issuer(issuer).issuedAt(ahora).expiresAt(ahora.plusSeconds(3600))
      .subject(username).claim("scope","USER").build();
    JwsHeader header=JwsHeader.with(MacAlgorithm.HS256).build();
    return encoder.encode(JwtEncoderParameters.from(header,claims)).getTokenValue();
  }
}

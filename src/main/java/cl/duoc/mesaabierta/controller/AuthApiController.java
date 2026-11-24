package cl.duoc.mesaabierta.controller;
import cl.duoc.mesaabierta.service.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController @RequestMapping("/api/auth")
public class AuthApiController {
  private final AuthenticationManager auth;private final JwtService jwt;
  public AuthApiController(AuthenticationManager auth,JwtService jwt){this.auth=auth;this.jwt=jwt;}
  public record Credenciales(@NotBlank String username,@NotBlank String password){}
  public record TokenRespuesta(String accessToken,String tokenType,int expiresIn){}
  @PostMapping("/login") public TokenRespuesta login(@Valid @RequestBody Credenciales datos){
    try{auth.authenticate(new UsernamePasswordAuthenticationToken(datos.username(),datos.password()));}
    catch(AuthenticationException ex){throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Credenciales inválidas");}
    return new TokenRespuesta(jwt.emitir(datos.username()),"Bearer",3600);
  }
}

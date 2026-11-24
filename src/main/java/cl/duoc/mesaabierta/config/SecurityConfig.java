package cl.duoc.mesaabierta.config;
import cl.duoc.mesaabierta.service.UsuarioDetailsService;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

@Configuration
public class SecurityConfig {
  @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder(12);}
  @Bean AuthenticationManager authenticationManager(UsuarioDetailsService service,PasswordEncoder encoder){
    var provider=new DaoAuthenticationProvider(service);
    provider.setPasswordEncoder(encoder);
    return new ProviderManager(provider);
  }
  @Bean SecretKey jwtKey(@Value("${app.jwt.secret-base64}") String encoded){
    if(encoded==null||encoded.isBlank())throw new IllegalStateException("Falta JWT_SECRET_BASE64 en .env");
    byte[] bytes;
    try {bytes=Base64.getDecoder().decode(encoded);}catch(IllegalArgumentException ex){throw new IllegalStateException("JWT_SECRET_BASE64 invalido",ex);}
    if(bytes.length<32)throw new IllegalStateException("JWT_SECRET_BASE64 debe contener al menos 32 bytes aleatorios");
    return new SecretKeySpec(bytes,"HmacSHA256");
  }
  @Bean JwtEncoder jwtEncoder(SecretKey key){
    return NimbusJwtEncoder.withSecretKey(key).algorithm(MacAlgorithm.HS256).build();
  }
  @Bean JwtDecoder jwtDecoder(SecretKey key,@Value("${app.jwt.issuer}") String issuer){
    NimbusJwtDecoder decoder=NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
    decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(issuer));
    return decoder;
  }
  @Bean @Order(1) SecurityFilterChain apiChain(HttpSecurity http) throws Exception {
    http.securityMatcher("/api/**")
      .authorizeHttpRequests(auth->auth.requestMatchers(HttpMethod.POST,"/api/auth/login").permitAll()
        .requestMatchers(HttpMethod.GET,"/api/recetas").permitAll().anyRequest().authenticated())
      // API stateless: sólo acepta Bearer JWT, no cookies de sesión; por eso no necesita token CSRF.
      .csrf(csrf->csrf.disable())
      .sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .oauth2ResourceServer(oauth->oauth.jwt(Customizer.withDefaults()))
      .headers(h->h.contentSecurityPolicy(c->c.policyDirectives("default-src 'none'; frame-ancestors 'none'")));
    return http.build();
  }
  @Bean @Order(2) SecurityFilterChain webChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth->auth
        .requestMatchers("/","/buscar","/login","/error","/css/**","/js/**","/images/**","/favicon.svg").permitAll()
        .requestMatchers("/receta/**","/media/**","/publicar","/compartidas").authenticated()
        .anyRequest().denyAll())
      .formLogin(form->form.loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/",false)
         .failureUrl("/login?error").permitAll())
      .logout(logout->logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout")
         .invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll())
      // CSRF ACTIVADO en todas las acciones web POST (formularios Thymeleaf).
      .headers(headers->headers.frameOptions(frame->frame.deny())
        .contentSecurityPolicy(csp->csp.policyDirectives("default-src 'self'; base-uri 'self'; form-action 'self'; frame-ancestors 'none'; object-src 'none'; script-src 'self'; style-src 'self'; img-src 'self' https://images.unsplash.com data:; media-src 'self'; font-src 'self'; connect-src 'self'"))
        .referrerPolicy(referrer->referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)));
    return http.build();
  }
}

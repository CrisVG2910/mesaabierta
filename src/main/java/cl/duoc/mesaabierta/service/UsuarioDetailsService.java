package cl.duoc.mesaabierta.service;
import cl.duoc.mesaabierta.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
@Service
public class UsuarioDetailsService implements UserDetailsService {
  private final UsuarioRepository usuarios;
  public UsuarioDetailsService(UsuarioRepository usuarios){this.usuarios=usuarios;}
  @Override public UserDetails loadUserByUsername(String username) {
    var u=usuarios.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("Usuario no encontrado"));
    return User.withUsername(u.username).password(u.passwordHash).roles("USER").disabled(!u.enabled).build();
  }
}

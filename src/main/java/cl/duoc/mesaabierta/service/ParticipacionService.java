package cl.duoc.mesaabierta.service;
import cl.duoc.mesaabierta.model.*;
import cl.duoc.mesaabierta.repository.*;
import jakarta.validation.constraints.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.Instant;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ParticipacionService {
  private final RecetaRepository recetas;private final UsuarioRepository usuarios;
  private final ComentarioRepository comentarios;private final ValoracionRepository valoraciones;
  private final CompartidoRepository compartidos;private final RecetaService recetaService;
  private final String urlBase;
  public ParticipacionService(RecetaRepository recetas,UsuarioRepository usuarios,ComentarioRepository comentarios,
      ValoracionRepository valoraciones,CompartidoRepository compartidos,RecetaService recetaService,
      @Value("${app.public-base-url}") String urlBase){
    this.recetas=recetas;this.usuarios=usuarios;this.comentarios=comentarios;this.valoraciones=valoraciones;
    this.compartidos=compartidos;this.recetaService=recetaService;this.urlBase=urlBase.replaceAll("/+$","");
  }
  public record NuevaReceta(@NotBlank @Size(min=5,max=160) String nombre,
       @NotBlank @Size(max=100) String cocina, @NotBlank @Size(max=100) String pais,
       @NotBlank String dificultad,@Min(1) @Max(1440) int minutos,@Min(1) @Max(24) int porciones,
       @NotBlank @Size(max=1000) String resumen,@NotBlank @Size(max=4000) String introduccion,
       @NotBlank @Size(max=4000) String ingredientes,@NotBlank @Size(max=7000) String pasos) {}
  public record ComentarioVista(Long id,String usuario,String texto,Instant fecha) {}
  public record ValoracionVista(double promedio,int cantidad) {}
  public record CompartidoVista(String receta,String titulo,String autor,Instant fecha) {}
  public record CompartirRespuesta(String plataforma,String enlace) {}
  public Usuario usuario(String username){return usuarios.findByUsername(username)
      .orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Usuario no disponible"));}
  public RecetaEntity entidad(String slug){return recetas.findBySlug(slug)
      .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Receta no encontrada"));}
  private boolean esPropietario(RecetaEntity receta, String username) {
    return username != null && receta.owner != null
        && username.equals(receta.owner.getUsername());
  }
  public void exigirPropietario(RecetaEntity receta,String username){
    if(!esPropietario(receta,username))
      throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Sólo el autor puede hacerlo");
  }
  @Transactional
  public Receta crear(NuevaReceta nueva,String username){
    if(!List.of("Fácil","Media","Difícil").contains(nueva.dificultad()))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Dificultad inválida");
    RecetaEntity r=RecetaEntity.nueva();
    r.nombre=nueva.nombre().strip();r.slug=slugUnico(nueva.nombre());r.cocina=nueva.cocina().strip();
    r.pais=nueva.pais().strip();r.dificultad=nueva.dificultad();r.minutos=nueva.minutos();r.porciones=nueva.porciones();
    r.resumen=nueva.resumen().strip();r.introduccion=nueva.introduccion().strip();
    r.ingredientesTxt=limpiarLineas(nueva.ingredientes());r.pasosTxt=limpiarLineas(nueva.pasos());
    r.etiquetasTxt="Comunidad\nNuevo";r.imagen="/recetas/images/food-fallback.svg";
    r.fotoAlt="Imagen ilustrativa de receta";r.popularidad=1;r.reciente=true;r.owner=usuario(username);
    recetas.saveAndFlush(r);
    return recetaService.convertir(r);
  }
  private String limpiarLineas(String s){return String.join("\n",s.lines().map(String::strip).filter(x->!x.isBlank()).limit(30).toList());}
  private String slugUnico(String nombre){
    String s=Normalizer.normalize(nombre,Normalizer.Form.NFD).replaceAll("\\p{M}","")
      .toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+","-").replaceAll("^-|-$","");
    if(s.isBlank())s="receta";
    s=s.substring(0,Math.min(s.length(),125));String candidato=s;int i=2;
    while(recetas.existsBySlug(candidato)){candidato=s+"-"+i++;}
    return candidato;
  }
  @Transactional
  public ComentarioVista comentar(String slug,String username,String texto){
    if(texto==null||texto.isBlank()||texto.length()>1000)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Comentario de 1 a 1000 caracteres");
    var c=comentarios.save(ComentarioEntity.nuevo(entidad(slug),usuario(username),texto.strip()));
    return new ComentarioVista(c.id,username,c.texto,c.createdAt);
  }
  @Transactional(readOnly=true)
  public List<ComentarioVista> comentarios(String slug){
    return comentarios.findByReceta_IdOrderByCreatedAtDesc(entidad(slug).id).stream()
      .map(c->new ComentarioVista(c.id,c.usuario.getUsername(),c.texto,c.createdAt)).toList();
  }
  @Transactional
  public ValoracionVista valorar(String slug,String username,int puntuacion){
    if(puntuacion<1||puntuacion>5)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Valoración entre 1 y 5");
    var receta=entidad(slug);var u=usuario(username);
    var v=valoraciones.findByReceta_IdAndUsuario_Id(receta.id,u.id).orElseGet(()->ValoracionEntity.nueva(receta,u,puntuacion));
    v.puntuacion=puntuacion;v.updatedAt=Instant.now();valoraciones.save(v);
    return promedio(receta.id);
  }
  @Transactional(readOnly=true)
  public ValoracionVista promedio(String slug){return promedio(entidad(slug).id);}
  private ValoracionVista promedio(Long id){var items=valoraciones.findByReceta_Id(id);
    return new ValoracionVista(items.stream().mapToInt(v->v.puntuacion).average().orElse(0),items.size());}
  @Transactional
  public CompartirRespuesta compartir(String slug,String username,String plataforma){
    if(!Set.of("SITIO","X","WHATSAPP","FACEBOOK").contains(plataforma))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Plataforma no permitida");
    var receta=entidad(slug);exigirPropietario(receta,username);
    compartidos.save(CompartidoEntity.nuevo(receta,usuario(username),plataforma));
    String enlace=urlBase+"/receta/"+receta.slug;
    String url=URLEncoder.encode(enlace,StandardCharsets.UTF_8);
    return new CompartirRespuesta(plataforma,switch(plataforma){
      case "X" -> "https://twitter.com/intent/tweet?url="+url;
      case "WHATSAPP" -> "https://api.whatsapp.com/send?text="+url;
      case "FACEBOOK" -> "https://www.facebook.com/sharer/sharer.php?u="+url;
      default -> enlace;
    });
  }
  @Transactional(readOnly=true)
  public List<CompartidoVista> compartidas(){return compartidos.findByPlataformaOrderByCreatedAtDesc("SITIO").stream()
    .map(c->new CompartidoVista(c.receta.getSlug(),c.receta.getNombre(),c.usuario.getUsername(),c.createdAt)).toList();}
  @Transactional(readOnly=true)
  public boolean propietario(String slug,String username){return esPropietario(entidad(slug),username);}
}

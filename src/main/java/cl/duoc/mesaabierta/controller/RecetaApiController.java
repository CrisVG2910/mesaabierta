package cl.duoc.mesaabierta.controller;
import cl.duoc.mesaabierta.model.Receta;
import cl.duoc.mesaabierta.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.security.Principal;
import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController @RequestMapping("/api/recetas")
public class RecetaApiController {
  private final RecetaService recetas;private final ParticipacionService participacion;private final ArchivoService archivos;
  public RecetaApiController(RecetaService recetas,ParticipacionService participacion,ArchivoService archivos){
    this.recetas=recetas;this.participacion=participacion;this.archivos=archivos;
  }
  // Listado público expone sólo resumen, no ingredientes ni instrucciones privadas.
  public record ResumenReceta(String slug,String nombre,String cocina,String pais,String dificultad,int minutos,String imagen,String resumen) {
    static ResumenReceta de(Receta r){return new ResumenReceta(r.slug(),r.nombre(),r.cocina(),r.pais(),r.dificultad(),r.minutos(),r.imagen(),r.resumen());}
  }
  @GetMapping public List<ResumenReceta> listar(@RequestParam(defaultValue="") String nombre,
      @RequestParam(defaultValue="") String cocina,@RequestParam(defaultValue="") String ingrediente,
      @RequestParam(defaultValue="") String pais,@RequestParam(defaultValue="") String dificultad){
    return recetas.buscar(nombre,cocina,ingrediente,pais,dificultad).stream().map(ResumenReceta::de).toList();
  }
  @GetMapping("/{slug}") public Receta detalle(@PathVariable String slug){return recetas.porSlug(slug)
    .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));}
  @PostMapping public ResponseEntity<Receta> crear(@Valid @RequestBody ParticipacionService.NuevaReceta datos,Principal usuario){
    return ResponseEntity.status(HttpStatus.CREATED).body(participacion.crear(datos,usuario.getName()));
  }
  public record ComentarioRequest(@NotBlank @Size(max=1000) String texto){}
  public record ValoracionRequest(@Min(1) @Max(5) int puntuacion){}
  public record CompartirRequest(@NotBlank String plataforma){}
  @GetMapping("/{slug}/comentarios") public List<ParticipacionService.ComentarioVista> comentarios(@PathVariable String slug){
    return participacion.comentarios(slug);
  }
  @PostMapping("/{slug}/comentarios") public ResponseEntity<ParticipacionService.ComentarioVista> comentar(
    @PathVariable String slug,@Valid @RequestBody ComentarioRequest datos,Principal usuario){
    return ResponseEntity.status(HttpStatus.CREATED).body(participacion.comentar(slug,usuario.getName(),datos.texto()));
  }
  @PutMapping("/{slug}/valoracion") public ParticipacionService.ValoracionVista valorar(
      @PathVariable String slug,@Valid @RequestBody ValoracionRequest datos,Principal usuario){
    return participacion.valorar(slug,usuario.getName(),datos.puntuacion());
  }
  @GetMapping("/{slug}/valoracion") public ParticipacionService.ValoracionVista promedio(@PathVariable String slug){
    return participacion.promedio(slug);
  }
  @PostMapping(path="/{slug}/archivos",consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ArchivoService.ArchivoVista> subir(@PathVariable String slug,
      @RequestPart("archivo") MultipartFile archivo,Principal usuario){
    return ResponseEntity.status(HttpStatus.CREATED).body(archivos.guardar(slug,usuario.getName(),archivo));
  }
  @GetMapping("/{slug}/archivos") public List<ArchivoService.ArchivoVista> archivos(@PathVariable String slug){
    return archivos.listar(slug);
  }
  @PostMapping("/{slug}/compartir") public ParticipacionService.CompartirRespuesta compartir(
      @PathVariable String slug,@Valid @RequestBody CompartirRequest request,Principal usuario){
    return participacion.compartir(slug,usuario.getName(),request.plataforma());
  }
}

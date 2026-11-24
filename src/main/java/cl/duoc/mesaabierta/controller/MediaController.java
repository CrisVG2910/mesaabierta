package cl.duoc.mesaabierta.controller;
import cl.duoc.mesaabierta.service.ArchivoService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
public class MediaController {
  private final ArchivoService archivos;
  public MediaController(ArchivoService archivos){this.archivos=archivos;}
  @GetMapping("/media/{id}") public ResponseEntity<Resource> servir(@PathVariable Long id){return archivos.obtener(id);}
  // Misma descarga protegida por la cadena stateless JWT, para clientes REST.
  @GetMapping("/api/media/{id}") public ResponseEntity<Resource> servirApi(@PathVariable Long id){return archivos.obtener(id);}
}

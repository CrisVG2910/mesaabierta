package cl.duoc.mesaabierta.controller;
import cl.duoc.mesaabierta.service.*;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ParticipacionWebController {
  private final ParticipacionService participacion;
  private final ArchivoService archivos;
  public ParticipacionWebController(ParticipacionService participacion,ArchivoService archivos){
    this.participacion=participacion;this.archivos=archivos;
  }
  @GetMapping("/publicar") public String nueva(){return "publicar";}
  @PostMapping("/publicar") public String publicar(@Valid @ModelAttribute ParticipacionService.NuevaReceta datos,
      Principal usuario){return "redirect:/receta/"+participacion.crear(datos,usuario.getName()).slug();}
  @GetMapping("/compartidas") public String compartidas(Model model){
    model.addAttribute("compartidas",participacion.compartidas());return "compartidas";
  }
  @PostMapping("/receta/{slug}/comentario") public String comentar(@PathVariable String slug,
    @RequestParam String texto,Principal usuario){
    participacion.comentar(slug,usuario.getName(),texto);
    return "redirect:/receta/"+slug+"#comunidad";
  }
  @PostMapping("/receta/{slug}/valorar") public String valorar(@PathVariable String slug,
      @RequestParam int puntuacion,Principal usuario){
    participacion.valorar(slug,usuario.getName(),puntuacion);
    return "redirect:/receta/"+slug+"#comunidad";
  }
  @PostMapping("/receta/{slug}/archivo") public String archivo(@PathVariable String slug,
      @RequestParam("archivo") MultipartFile archivo,Principal usuario){
    archivos.guardar(slug,usuario.getName(),archivo);
    return "redirect:/receta/"+slug+"#multimedia";
  }
  @PostMapping("/receta/{slug}/compartir") public String compartir(@PathVariable String slug,
      @RequestParam String plataforma,Principal usuario){
    var respuesta=participacion.compartir(slug,usuario.getName(),plataforma);
    if(plataforma.equals("SITIO"))return "redirect:/compartidas";
    return "redirect:"+respuesta.enlace();
  }
}

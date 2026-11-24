package cl.duoc.mesaabierta.controller;

import cl.duoc.mesaabierta.model.Receta;
import cl.duoc.mesaabierta.service.RecetaService;
import cl.duoc.mesaabierta.service.ParticipacionService;
import cl.duoc.mesaabierta.service.ArchivoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class PaginaController {
    private final RecetaService recetas;
    private final ParticipacionService participacion;
    private final ArchivoService archivos;

    public PaginaController(RecetaService recetas,ParticipacionService participacion,ArchivoService archivos) {
        this.recetas = recetas;this.participacion=participacion;this.archivos=archivos;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("recientes", recetas.recientes());
        model.addAttribute("populares", recetas.populares());
        model.addAttribute("destacada", recetas.destacada());
        model.addAttribute("totalRecetas", recetas.todas().size());
        return "inicio";
    }

    @GetMapping("/buscar")
    public String buscar(
        @RequestParam(defaultValue = "") String nombre,
        @RequestParam(defaultValue = "") String cocina,
        @RequestParam(defaultValue = "") String ingrediente,
        @RequestParam(defaultValue = "") String pais,
        @RequestParam(defaultValue = "") String dificultad,
        Model model
    ) {
        var resultados = recetas.buscar(nombre, cocina, ingrediente, pais, dificultad);
        model.addAttribute("recetas", resultados);
        model.addAttribute("nombre", nombre);
        model.addAttribute("cocina", cocina);
        model.addAttribute("ingrediente", ingrediente);
        model.addAttribute("pais", pais);
        model.addAttribute("dificultad", dificultad);
        model.addAttribute("cocinas", recetas.cocinas());
        model.addAttribute("paises", recetas.paises());
        model.addAttribute("dificultades", recetas.dificultades());
        model.addAttribute("filtrando", !nombre.isBlank() || !cocina.isBlank() || !ingrediente.isBlank()
            || !pais.isBlank() || !dificultad.isBlank());
        return "buscar";
    }

    @GetMapping("/receta/{slug}")
    public String detalle(@PathVariable String slug, Model model, java.security.Principal usuario) {
        Receta receta = recetas.porSlug(slug).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receta no encontrada")
        );
        model.addAttribute("receta", receta);
        model.addAttribute("relacionadas", recetas.relacionadas(receta));
        model.addAttribute("comentarios", participacion.comentarios(slug));
        model.addAttribute("valoracion", participacion.promedio(slug));
        model.addAttribute("archivos", archivos.listar(slug));
        model.addAttribute("esAutor", participacion.propietario(slug,usuario.getName()));
        return "detalle";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}

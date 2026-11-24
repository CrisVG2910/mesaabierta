package cl.duoc.mesaabierta.service;
import cl.duoc.mesaabierta.model.*;
import cl.duoc.mesaabierta.repository.RecetaRepository;
import java.text.Normalizer;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service @Transactional(readOnly=true)
public class RecetaService {
  private final RecetaRepository repo;
  public RecetaService(RecetaRepository repo){this.repo=repo;}
  public List<Receta> todas(){return repo.findAll().stream().map(this::convertir).sorted(Comparator.comparing(Receta::slug)).toList();}
  public Receta destacada(){return porSlug("pasta-pesto").orElseGet(()->todas().getFirst());}
  public List<Receta> recientes(){return todas().stream().filter(Receta::reciente).limit(4).toList();}
  public List<Receta> populares(){return todas().stream().sorted(Comparator.comparingInt(Receta::popularidad).reversed()).limit(4).toList();}
  public Optional<Receta> porSlug(String slug){return repo.findBySlug(slug).map(this::convertir);}
  public List<Receta> relacionadas(Receta actual){return todas().stream().filter(r->!r.slug().equals(actual.slug()))
    .sorted(Comparator.comparingInt((Receta r)->r.cocina().equals(actual.cocina())?1:0).reversed()).limit(3).toList();}
  public List<String> cocinas(){return todas().stream().map(Receta::cocina).distinct().sorted().toList();}
  public List<String> paises(){return todas().stream().map(Receta::pais).distinct().sorted().toList();}
  public List<String> dificultades(){return List.of("Fácil","Media","Difícil");}
  public List<Receta> buscar(String nombre,String cocina,String ingrediente,String pais,String dificultad){
    return todas().stream().filter(r->coincide(r.nombre(),nombre)).filter(r->coincide(r.cocina(),cocina))
      .filter(r->r.ingredientes().stream().anyMatch(i->coincide(i,ingrediente)))
      .filter(r->coincide(r.pais(),pais)).filter(r->coincide(r.dificultad(),dificultad)).toList();
  }
  private boolean coincide(String valor,String filtro){return filtro==null||filtro.isBlank()||normal(valor).contains(normal(filtro));}
  private String normal(String t){return Normalizer.normalize(t,Normalizer.Form.NFD).replaceAll("\\p{M}","").toLowerCase(Locale.ROOT).trim();}
  public Receta convertir(RecetaEntity e){return new Receta(e.slug,e.nombre,e.cocina,e.pais,e.dificultad,e.minutos,e.porciones,
     e.resumen,e.introduccion,e.imagen,e.fotoAlt,lines(e.ingredientesTxt),lines(e.pasosTxt),lines(e.etiquetasTxt),e.popularidad,e.reciente);}
  private List<String> lines(String s){return s==null||s.isBlank()?List.of():Arrays.asList(s.split("\n"));}
}

package cl.duoc.mesaabierta.service;
import cl.duoc.mesaabierta.model.*;
import cl.duoc.mesaabierta.repository.*;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
@Configuration
public class DataSeeder {
  @Bean ApplicationRunner semillas(UsuarioRepository usuarios,RecetaRepository recetas) {
    return args -> poblar(usuarios,recetas);
  }
  @Transactional
  public void poblar(UsuarioRepository usuarios, RecetaRepository recetas) {
    // Hashes BCrypt obtenidos del proyecto original; nunca se almacena texto plano.
    Usuario ana=asegurar(usuarios,"chefana","$2a$12$cDyk/L0U63jxOjsNbCwS3.gkx2NgBC8Lvhs.WANbxx6VFev9OnLiu","Ana Cocina");
    Usuario leo=asegurar(usuarios,"chefleo","$2a$12$FxosuYJIgU/YDxh5rPGd6u73CCaqrqD8seDTUu7NZhtLuRVJMhZ0W","Leo Fogones");
    Usuario mia=asegurar(usuarios,"chefmia","$2a$12$CeLVWiHpsBd3jYdBO6kFfe2xHFnrJT2cOXHquRQFTVv.yz03BX1M6","Mia Sabores");
    Usuario[] autores={ana,leo,mia};
    int i=0;
    for (Receta r : new CatalogoInicial().todas()) {
      if (recetas.existsBySlug(r.slug())) {i++;continue;}
      RecetaEntity e=RecetaEntity.nueva();
      e.slug=r.slug();e.nombre=r.nombre();e.cocina=r.cocina();e.pais=r.pais();
      e.dificultad=r.dificultad();e.minutos=r.minutos();e.porciones=r.porciones();
      e.resumen=r.resumen();e.introduccion=r.introduccion();e.imagen=r.imagen();e.fotoAlt=r.fotoAlt();
      e.ingredientesTxt=String.join("\n",r.ingredientes());e.pasosTxt=String.join("\n",r.pasos());
      e.etiquetasTxt=String.join("\n",r.etiquetas());e.popularidad=r.popularidad();
      e.reciente=r.reciente();e.owner=autores[i++%autores.length];
      recetas.save(e);
    }
  }
  private Usuario asegurar(UsuarioRepository repos,String username,String hash,String nombre) {
    return repos.findByUsername(username).orElseGet(()->repos.save(new Usuario(username,hash,nombre)));
  }
}

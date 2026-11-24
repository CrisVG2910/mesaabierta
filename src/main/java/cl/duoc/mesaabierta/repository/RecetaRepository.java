package cl.duoc.mesaabierta.repository;
import cl.duoc.mesaabierta.model.RecetaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RecetaRepository extends JpaRepository<RecetaEntity,Long> {
  Optional<RecetaEntity> findBySlug(String slug);
  boolean existsBySlug(String slug);
}

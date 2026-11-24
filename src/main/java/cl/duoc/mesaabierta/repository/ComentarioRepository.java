package cl.duoc.mesaabierta.repository;
import cl.duoc.mesaabierta.model.ComentarioEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ComentarioRepository extends JpaRepository<ComentarioEntity,Long> {
  List<ComentarioEntity> findByReceta_IdOrderByCreatedAtDesc(Long id);
}

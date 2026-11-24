package cl.duoc.mesaabierta.repository;
import cl.duoc.mesaabierta.model.ArchivoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ArchivoRepository extends JpaRepository<ArchivoEntity,Long> {
  List<ArchivoEntity> findByReceta_IdOrderByCreatedAtDesc(Long id);
}

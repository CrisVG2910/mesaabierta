package cl.duoc.mesaabierta.repository;
import cl.duoc.mesaabierta.model.CompartidoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CompartidoRepository extends JpaRepository<CompartidoEntity,Long> {
  List<CompartidoEntity> findByPlataformaOrderByCreatedAtDesc(String plataforma);
}

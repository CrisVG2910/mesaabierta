package cl.duoc.mesaabierta.repository;
import cl.duoc.mesaabierta.model.ValoracionEntity;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ValoracionRepository extends JpaRepository<ValoracionEntity,Long> {
  Optional<ValoracionEntity> findByReceta_IdAndUsuario_Id(Long recetaId,Long usuarioId);
  List<ValoracionEntity> findByReceta_Id(Long recetaId);
}

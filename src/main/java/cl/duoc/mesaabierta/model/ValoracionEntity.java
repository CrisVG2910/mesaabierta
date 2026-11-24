package cl.duoc.mesaabierta.model;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="valoracion",uniqueConstraints=@UniqueConstraint(columnNames={"receta_id","usuario_id"}))
public class ValoracionEntity {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="receta_id",nullable=false) public RecetaEntity receta;
  @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="usuario_id",nullable=false) public Usuario usuario;
  @Column(nullable=false) public int puntuacion;
  @Column(name="updated_at",nullable=false) public Instant updatedAt=Instant.now();
  protected ValoracionEntity() {}
  public static ValoracionEntity nueva(RecetaEntity receta,Usuario usuario,int puntuacion){
    ValoracionEntity v=new ValoracionEntity();v.receta=receta;v.usuario=usuario;v.puntuacion=puntuacion;return v;
  }
}

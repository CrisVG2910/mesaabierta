package cl.duoc.mesaabierta.model;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="compartido")
public class CompartidoEntity {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="receta_id",nullable=false) public RecetaEntity receta;
  @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="usuario_id",nullable=false) public Usuario usuario;
  @Column(nullable=false,length=20) public String plataforma;
  @Column(name="created_at",nullable=false) public Instant createdAt=Instant.now();
  protected CompartidoEntity() {}
  public static CompartidoEntity nuevo(RecetaEntity r,Usuario u,String plataforma){
    CompartidoEntity c=new CompartidoEntity();c.receta=r;c.usuario=u;c.plataforma=plataforma;return c;
  }
}

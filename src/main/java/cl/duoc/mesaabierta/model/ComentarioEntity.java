package cl.duoc.mesaabierta.model;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="comentario")
public class ComentarioEntity {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="receta_id",nullable=false) public RecetaEntity receta;
  @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="usuario_id",nullable=false) public Usuario usuario;
  @Column(nullable=false,length=1000) public String texto;
  @Column(name="created_at",nullable=false) public Instant createdAt=Instant.now();
  protected ComentarioEntity() {}
  public static ComentarioEntity nuevo(RecetaEntity receta,Usuario usuario,String texto){
    ComentarioEntity c=new ComentarioEntity();c.receta=receta;c.usuario=usuario;c.texto=texto;return c;
  }
}

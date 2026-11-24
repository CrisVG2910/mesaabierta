package cl.duoc.mesaabierta.model;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="archivo")
public class ArchivoEntity {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="receta_id",nullable=false) public RecetaEntity receta;
  @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="usuario_id",nullable=false) public Usuario usuario;
  @Column(name="nombre_original",nullable=false,length=160) public String nombreOriginal;
  @Column(name="nombre_guardado",nullable=false,length=80,unique=true) public String nombreGuardado;
  @Column(name="mime_type",nullable=false,length=40) public String mimeType;
  @Column(nullable=false,length=10) public String tipo;
  @Column(name="tamanio_bytes",nullable=false) public long tamanioBytes;
  @Column(name="created_at",nullable=false) public Instant createdAt=Instant.now();
  protected ArchivoEntity() {}
  public static ArchivoEntity nuevo(){return new ArchivoEntity();}
}

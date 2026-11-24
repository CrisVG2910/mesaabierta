package cl.duoc.mesaabierta.model;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="receta")
public class RecetaEntity {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  @Column(nullable=false,unique=true,length=160) public String slug;
  @Column(nullable=false,length=160) public String nombre;
  @Column(nullable=false,length=100) public String cocina;
  @Column(nullable=false,length=100) public String pais;
  @Column(nullable=false,length=20) public String dificultad;
  @Column(nullable=false) public int minutos;
  @Column(nullable=false) public int porciones;
  @Column(length=1000) public String resumen;
  @Column(columnDefinition="TEXT") public String introduccion;
  @Column(length=1000) public String imagen;
  @Column(name="foto_alt",length=255) public String fotoAlt;
  @Column(name="ingredientes_txt",columnDefinition="TEXT") public String ingredientesTxt;
  @Column(name="pasos_txt",columnDefinition="TEXT") public String pasosTxt;
  @Column(name="etiquetas_txt",columnDefinition="TEXT") public String etiquetasTxt;
  public int popularidad;
  public boolean reciente;
  @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="owner_id",nullable=false)
  public Usuario owner;
  @Column(name="created_at",nullable=false) public Instant createdAt=Instant.now();
  public String getSlug() { return slug; }
  public String getNombre() { return nombre; }
  protected RecetaEntity() {}
  public static RecetaEntity nueva() { return new RecetaEntity(); }
}

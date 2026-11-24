package cl.duoc.mesaabierta.model;
import jakarta.persistence.*;
@Entity @Table(name="app_user")
public class Usuario {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
  @Column(nullable=false,unique=true,length=60) public String username;
  @Column(name="password_hash",nullable=false,length=100) public String passwordHash;
  @Column(name="display_name",nullable=false,length=120) public String displayName;
  @Column(nullable=false) public boolean enabled=true;
  public String getUsername() { return username; }
  protected Usuario() {}
  public Usuario(String username,String passwordHash,String displayName) {
    this.username=username; this.passwordHash=passwordHash; this.displayName=displayName;
  }
}

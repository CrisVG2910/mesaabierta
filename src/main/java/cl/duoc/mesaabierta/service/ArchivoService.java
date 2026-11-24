package cl.duoc.mesaabierta.service;
import cl.duoc.mesaabierta.model.*;
import cl.duoc.mesaabierta.repository.ArchivoRepository;
import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ArchivoService {
  private final ArchivoRepository repo;private final ParticipacionService participacion;private final Path base;
  public ArchivoService(ArchivoRepository repo,ParticipacionService participacion,@Value("${app.media.storage-dir}") String dir){
    this.repo=repo;this.participacion=participacion;this.base=Path.of(dir).toAbsolutePath().normalize();
  }
  public record ArchivoVista(Long id,String nombre,String tipo,String url,String apiUrl,long tamanio) {}
  @Transactional
  public ArchivoVista guardar(String slug,String username,MultipartFile archivo){
    var receta=participacion.entidad(slug);participacion.exigirPropietario(receta,username);
    if(archivo.isEmpty()||archivo.getSize()>20L*1024*1024)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"El archivo debe pesar entre 1 byte y 20 MB");
    String mime;
    try(InputStream in=archivo.getInputStream()) {mime=tipoReal(in.readNBytes(16));}
    catch(IOException e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No se pudo leer el archivo");}
    if(mime==null)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Sólo JPG, PNG, WebP, MP4 y WebM");
    boolean video=mime.startsWith("video/");
    if(!video&&archivo.getSize()>5L*1024*1024)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Imagen: máximo 5 MB");
    String extension=switch(mime){case "image/jpeg"->".jpg";case "image/png"->".png";
      case "image/webp"->".webp";case "video/mp4"->".mp4";default->".webm";};
    String guardado=UUID.randomUUID()+extension;
    try{Files.createDirectories(base);try(InputStream in=archivo.getInputStream()){
      Files.copy(in, base.resolve(guardado));
    }}catch(IOException ex){throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"No se pudo almacenar");}
    var a=ArchivoEntity.nuevo();a.receta=receta;a.usuario=participacion.usuario(username);
    String original=Optional.ofNullable(archivo.getOriginalFilename()).orElse("archivo");
    a.nombreOriginal=original.replace('/','_').replace((char)92,'_').replace((char)10,'_').replace((char)13,'_');
    if(a.nombreOriginal.length()>160)a.nombreOriginal=a.nombreOriginal.substring(0,160);
    a.nombreGuardado=guardado;a.mimeType=mime;a.tipo=video?"VIDEO":"FOTO";a.tamanioBytes=archivo.getSize();
    repo.saveAndFlush(a);
    return vista(a);
  }
  private String tipoReal(byte[] b){
    if(b.length>=3&&(b[0]&255)==255&&(b[1]&255)==216&&(b[2]&255)==255)return "image/jpeg";
    if(b.length>=8&&(b[0]&255)==137&&b[1]==80&&b[2]==78&&b[3]==71)return "image/png";
    if(b.length>=12&&ascii(b,0,"RIFF")&&ascii(b,8,"WEBP"))return "image/webp";
    if(b.length>=8&&ascii(b,4,"ftyp"))return "video/mp4";
    if(b.length>=4&&(b[0]&255)==0x1a&&(b[1]&255)==0x45&&(b[2]&255)==0xdf&&(b[3]&255)==0xa3)return "video/webm";
    return null;
  }
  private boolean ascii(byte[] b,int pos,String text){for(int i=0;i<text.length();i++)if(b[pos+i]!=(byte)text.charAt(i))return false;return true;}
  public ArchivoVista vista(ArchivoEntity a){return new ArchivoVista(a.id,a.nombreOriginal,a.tipo,"/recetas/media/"+a.id,"/recetas/api/media/"+a.id,a.tamanioBytes);}
  @Transactional(readOnly=true)
  public List<ArchivoVista> listar(String slug){return repo.findByReceta_IdOrderByCreatedAtDesc(participacion.entidad(slug).id)
    .stream().map(this::vista).toList();}
  @Transactional(readOnly=true)
  public ResponseEntity<Resource> obtener(Long id){
    ArchivoEntity a=repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    Path archivo=base.resolve(a.nombreGuardado).normalize();
    if(!archivo.startsWith(base)||!Files.isRegularFile(archivo))throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return ResponseEntity.ok().contentType(MediaType.parseMediaType(a.mimeType))
      .header(HttpHeaders.CACHE_CONTROL,"private, no-store")
      .header(HttpHeaders.CONTENT_DISPOSITION,"inline")
      .body(new FileSystemResource(archivo));
  }
}

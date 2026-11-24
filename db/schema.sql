-- MySQL 8.4; DDL documental/reproducible (Hibernate también crea/actualiza tablas al iniciar).
-- Si ya levantaste Docker Compose, NO necesitas ejecutar este archivo manualmente.
CREATE DATABASE IF NOT EXISTS mesaabierta CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mesaabierta;
CREATE TABLE IF NOT EXISTS app_user (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(60) NOT NULL UNIQUE,
 password_hash VARCHAR(100) NOT NULL, display_name VARCHAR(120) NOT NULL, enabled BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS receta (
 id BIGINT PRIMARY KEY AUTO_INCREMENT, slug VARCHAR(160) NOT NULL UNIQUE,
 nombre VARCHAR(160) NOT NULL,cocina VARCHAR(100) NOT NULL,pais VARCHAR(100) NOT NULL,
 dificultad VARCHAR(20) NOT NULL,minutos INT NOT NULL,porciones INT NOT NULL,
 resumen VARCHAR(1000),introduccion TEXT,imagen VARCHAR(1000),foto_alt VARCHAR(255),
 ingredientes_txt TEXT,pasos_txt TEXT,etiquetas_txt TEXT,popularidad INT NOT NULL DEFAULT 0,
 reciente BOOLEAN NOT NULL DEFAULT TRUE,owner_id BIGINT NOT NULL,created_at DATETIME(6) NOT NULL,
 CONSTRAINT fk_receta_owner FOREIGN KEY(owner_id) REFERENCES app_user(id)
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS comentario (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,receta_id BIGINT NOT NULL,usuario_id BIGINT NOT NULL,
 texto VARCHAR(1000) NOT NULL,created_at DATETIME(6) NOT NULL,
 CONSTRAINT fk_comentario_receta FOREIGN KEY(receta_id) REFERENCES receta(id),
 CONSTRAINT fk_comentario_usuario FOREIGN KEY(usuario_id) REFERENCES app_user(id)
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS valoracion (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,receta_id BIGINT NOT NULL,usuario_id BIGINT NOT NULL,
 puntuacion INT NOT NULL,updated_at DATETIME(6) NOT NULL,
 CONSTRAINT fk_valoracion_receta FOREIGN KEY(receta_id) REFERENCES receta(id),
 CONSTRAINT fk_valoracion_usuario FOREIGN KEY(usuario_id) REFERENCES app_user(id),
 CONSTRAINT uq_valoracion_usuario_receta UNIQUE(receta_id,usuario_id),
 CONSTRAINT chk_valoracion CHECK(puntuacion BETWEEN 1 AND 5)
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS archivo (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,receta_id BIGINT NOT NULL,usuario_id BIGINT NOT NULL,
 nombre_original VARCHAR(160) NOT NULL,nombre_guardado VARCHAR(80) NOT NULL UNIQUE,
 mime_type VARCHAR(40) NOT NULL,tipo VARCHAR(10) NOT NULL,tamanio_bytes BIGINT NOT NULL,
 created_at DATETIME(6) NOT NULL,
 CONSTRAINT fk_archivo_receta FOREIGN KEY(receta_id) REFERENCES receta(id),
 CONSTRAINT fk_archivo_usuario FOREIGN KEY(usuario_id) REFERENCES app_user(id)
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS compartido (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,receta_id BIGINT NOT NULL,usuario_id BIGINT NOT NULL,
 plataforma VARCHAR(20) NOT NULL,created_at DATETIME(6) NOT NULL,
 CONSTRAINT fk_compartido_receta FOREIGN KEY(receta_id) REFERENCES receta(id),
 CONSTRAINT fk_compartido_usuario FOREIGN KEY(usuario_id) REFERENCES app_user(id)
) ENGINE=InnoDB;

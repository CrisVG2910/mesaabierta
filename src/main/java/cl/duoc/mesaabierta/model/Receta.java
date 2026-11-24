package cl.duoc.mesaabierta.model;

import java.util.List;

// Datos estaticos, tal como se permite para esta primera entrega.
public record Receta(
    String slug,
    String nombre,
    String cocina,
    String pais,
    String dificultad,
    int minutos,
    int porciones,
    String resumen,
    String introduccion,
    String imagen,
    String fotoAlt,
    List<String> ingredientes,
    List<String> pasos,
    List<String> etiquetas,
    int popularidad,
    boolean reciente
) {}

package cl.duoc.mesaabierta.service;

import cl.duoc.mesaabierta.model.Receta;
import java.text.Normalizer;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


public class CatalogoInicial {
    // Catalogo de semillas para poblar MySQL una sola vez. El servicio web consulta la base.
    private static final List<Receta> RECETAS = List.of(
        new Receta(
            "pasta-pesto", "Pasta al pesto de albahaca", "Italiana", "Italia", "Fácil", 25, 2,
            "Albahaca fresca, parmesano y un toque de limón para una pasta inolvidable.",
            "Una receta simple que celebra los ingredientes frescos. La clave está en preparar el pesto en frío y mezclarlo con la pasta recién cocida.",
            "https://images.unsplash.com/photo-1473093295043-cdd812d0e601?auto=format&fit=crop&w=1300&q=85",
            "Plato de pasta con salsa y hojas verdes",
            List.of("200 g de pasta larga", "1 taza de albahaca fresca", "30 g de queso parmesano", "25 g de nueces o piñones", "1 diente de ajo", "60 ml de aceite de oliva", "Sal y pimienta", "Ralladura de medio limón"),
            List.of("Hierve abundante agua con sal y cocina la pasta hasta que esté al dente.", "Tritura la albahaca, el ajo, las nueces, el parmesano y el aceite hasta obtener una salsa.", "Reserva media taza del agua de cocción antes de escurrir la pasta.", "Mezcla la pasta caliente con el pesto y agrega un poco del agua reservada para emulsionar.", "Sirve con ralladura de limón, pimienta y un poco más de parmesano."),
            List.of("Vegetariana", "Almuerzo", "Pastas"), 98, true
        ),
        new Receta(
            "ramen-casero", "Ramen casero reconfortante", "Japonesa", "Japón", "Media", 55, 2,
            "Un caldo aromático, fideos suaves y vegetales llenos de sabor.",
            "Una versión accesible del ramen: caldo con jengibre, ajo y soya, coronado con ingredientes frescos.",
            "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?auto=format&fit=crop&w=1300&q=85",
            "Tazón de ramen con fideos y guarniciones",
            List.of("300 g de fideos para ramen", "750 ml de caldo de pollo o verduras", "2 cucharadas de salsa de soya", "1 trozo de jengibre fresco", "2 dientes de ajo", "2 huevos", "100 g de champiñones", "1 cebollín", "Aceite de sésamo"),
            List.of("Hierve los huevos durante 7 minutos, enfría, pela y reserva.", "Sofríe el ajo y el jengibre; añade el caldo y la salsa de soya.", "Cocina los champiñones en el caldo a fuego suave durante 15 minutos.", "Hierve los fideos por separado según sus instrucciones.", "Distribuye fideos y caldo en tazones; termina con huevo, cebollín y aceite de sésamo."),
            List.of("Caliente", "Cena", "Sopas"), 95, true
        ),
        new Receta(
            "pizza-margarita", "Pizza margarita artesanal", "Italiana", "Italia", "Media", 70, 4,
            "Masa dorada, tomate, mozzarella y hojas frescas de albahaca.",
            "La combinación clásica de Nápoles, con un horneado intenso para lograr bordes dorados y una base fina.",
            "https://images.unsplash.com/photo-1574071318508-1cdbab80d002?auto=format&fit=crop&w=1300&q=85",
            "Pizza margarita con queso y albahaca",
            List.of("350 g de harina", "220 ml de agua tibia", "7 g de levadura seca", "1 cucharadita de sal", "200 g de salsa de tomate", "200 g de mozzarella", "Hojas de albahaca", "Aceite de oliva"),
            List.of("Mezcla harina, levadura, agua y sal; amasa hasta conseguir una masa elástica.", "Deja reposar la masa 40 minutos en un recipiente cubierto.", "Precalienta el horno a 250 °C con una bandeja en su interior.", "Estira la masa y añade una capa delgada de tomate y mozzarella.", "Hornea entre 10 y 15 minutos; agrega albahaca y aceite de oliva al servir."),
            List.of("Horno", "Para compartir", "Clásicos"), 100, false
        ),
        new Receta(
            "ensalada-quinoa", "Ensalada fresca de quinoa", "Fusión", "Perú", "Fácil", 30, 2,
            "Una mezcla colorida de quinoa, palta, vegetales y cítricos.",
            "Perfecta para un almuerzo ligero pero completo, con texturas crujientes y aliño vibrante.",
            "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?auto=format&fit=crop&w=1300&q=85",
            "Ensalada variada con hojas verdes y vegetales",
            List.of("1 taza de quinoa", "2 tazas de agua", "1 palta", "1 pepino", "10 tomates cherry", "1 zanahoria", "Jugo de 1 limón", "2 cucharadas de aceite de oliva", "Sal y cilantro"),
            List.of("Lava la quinoa bajo agua fría para retirar su sabor amargo.", "Cocínala en dos tazas de agua durante 15 minutos y deja enfriar.", "Corta la palta, el pepino, los tomates y la zanahoria.", "Mezcla todos los ingredientes en una fuente amplia.", "Aliña con limón, aceite de oliva, sal y cilantro antes de servir."),
            List.of("Vegetariana", "Saludable", "Sin gluten"), 84, true
        ),
        new Receta(
            "tacos-pollo", "Tacos de pollo y lima", "Mexicana", "México", "Fácil", 35, 4,
            "Pollo especiado, tortillas calientes y salsa cremosa con lima.",
            "Un plato perfecto para compartir: cada comensal prepara sus tacos a su manera.",
            "https://images.unsplash.com/photo-1551504734-5ee1c4a1479b?auto=format&fit=crop&w=1300&q=85",
            "Tacos mexicanos rellenos con ingredientes frescos",
            List.of("400 g de pechuga de pollo", "8 tortillas pequeñas", "1 cucharadita de comino", "1 cucharadita de pimentón", "1 palta", "1 tomate", "1 cebolla morada", "2 limas", "Yogur natural y cilantro"),
            List.of("Corta el pollo en tiras y sazónalo con comino, pimentón y sal.", "Dora el pollo en sartén durante 8 a 10 minutos hasta que esté completamente cocido.", "Pica los vegetales y mezcla el yogur con el jugo de una lima.", "Calienta las tortillas en una sartén sin aceite.", "Arma los tacos con pollo, verduras, salsa y cilantro."),
            List.of("Para compartir", "Rápida", "Cena"), 92, true
        ),
        new Receta(
            "risotto-hongos", "Risotto cremoso de hongos", "Italiana", "Italia", "Media", 45, 3,
            "Arroz cremoso, champiñones dorados y un final de parmesano.",
            "El secreto está en añadir el caldo poco a poco y remover hasta conseguir una textura suave.",
            "https://images.unsplash.com/photo-1476124369491-e7addf5db371?auto=format&fit=crop&w=1300&q=85",
            "Arroz cremoso servido con setas",
            List.of("250 g de arroz arborio", "250 g de champiñones", "1 litro de caldo de verduras", "1 cebolla pequeña", "50 g de queso parmesano", "30 g de mantequilla", "Aceite de oliva", "Sal, pimienta y tomillo"),
            List.of("Calienta el caldo y mantenlo a fuego bajo.", "Saltea los champiñones en aceite hasta dorarlos y retíralos.", "Sofríe la cebolla, incorpora el arroz y remueve durante dos minutos.", "Añade el caldo caliente de a cucharones, removiendo y esperando a que se absorba antes de agregar más.", "Cuando el arroz esté cremoso, mezcla con champiñones, mantequilla, parmesano y tomillo."),
            List.of("Vegetariana", "Reconfortante", "Almuerzo"), 88, false
        ),
        new Receta(
            "shakshuka", "Shakshuka con tomates", "Mediterránea", "Túnez", "Fácil", 35, 3,
            "Huevos cocinados sobre una salsa especiada de tomate y pimentón.",
            "Ideal para brunch o cena: sirve con pan tostado para aprovechar toda la salsa.",
            "https://images.unsplash.com/photo-1590412200988-a436970781fa?auto=format&fit=crop&w=1300&q=85",
            "Preparación de huevos con tomate en sartén",
            List.of("4 huevos", "400 g de tomate triturado", "1 pimentón rojo", "1 cebolla", "2 dientes de ajo", "1 cucharadita de comino", "1 cucharadita de pimentón dulce", "Aceite de oliva", "Perejil y pan"),
            List.of("Sofríe la cebolla, el ajo y el pimentón picados.", "Agrega el tomate, el comino y el pimentón dulce; cocina 12 minutos.", "Forma cuatro huecos en la salsa y casca un huevo en cada uno.", "Tapa la sartén y cocina hasta que las claras estén firmes.", "Termina con perejil y sirve inmediatamente con pan."),
            List.of("Brunch", "Vegetariana", "Sartén"), 81, false
        ),
        new Receta(
            "brownies-chocolate", "Brownies de chocolate intenso", "Americana", "Estados Unidos", "Fácil", 40, 9,
            "Exterior delicado, centro húmedo y el sabor profundo del chocolate.",
            "Un clásico de repostería que funciona perfecto con helado o una taza de café.",
            "https://images.unsplash.com/photo-1606313564200-e75d5e30476c?auto=format&fit=crop&w=1300&q=85",
            "Brownies cuadrados de chocolate",
            List.of("180 g de chocolate amargo", "120 g de mantequilla", "150 g de azúcar", "3 huevos", "90 g de harina", "20 g de cacao amargo", "1 pizca de sal", "1 cucharadita de vainilla"),
            List.of("Precalienta el horno a 175 °C y forra un molde cuadrado con papel mantequilla.", "Derrite el chocolate con la mantequilla a baño María y deja templar.", "Bate huevos con azúcar y vainilla; incorpora el chocolate derretido.", "Agrega harina, cacao y sal con movimientos envolventes.", "Hornea 22 a 27 minutos y deja enfriar antes de cortar en cuadrados."),
            List.of("Postres", "Chocolate", "Horno"), 97, true
        )
    );

    public List<Receta> todas() { return RECETAS; }
    public Receta destacada() { return RECETAS.get(0); }
    public List<Receta> recientes() { return RECETAS.stream().filter(Receta::reciente).limit(4).toList(); }
    public List<Receta> populares() {
        return RECETAS.stream().sorted(Comparator.comparingInt(Receta::popularidad).reversed()).limit(4).toList();
    }
    public Optional<Receta> porSlug(String slug) {
        return RECETAS.stream().filter(r -> r.slug().equals(slug)).findFirst();
    }
    public List<Receta> relacionadas(Receta actual) {
        return RECETAS.stream().filter(r -> !r.slug().equals(actual.slug()))
            .sorted(Comparator.comparingInt((Receta r) -> r.cocina().equals(actual.cocina()) ? 1 : 0).reversed())
            .limit(3).toList();
    }
    public List<String> cocinas() { return RECETAS.stream().map(Receta::cocina).distinct().sorted().toList(); }
    public List<String> paises() { return RECETAS.stream().map(Receta::pais).distinct().sorted().toList(); }
    public List<String> dificultades() { return List.of("Fácil", "Media"); }

    public List<Receta> buscar(String nombre, String cocina, String ingrediente, String pais, String dificultad) {
        return RECETAS.stream().filter(r -> coincide(r.nombre(), nombre))
            .filter(r -> coincide(r.cocina(), cocina))
            .filter(r -> contieneIngrediente(r.ingredientes(), ingrediente))
            .filter(r -> coincide(r.pais(), pais))
            .filter(r -> coincide(r.dificultad(), dificultad))
            .toList();
    }

    private boolean contieneIngrediente(List<String> ingredientes, String filtro) {
        if (filtro == null || filtro.isBlank()) return true;
        return ingredientes.stream().anyMatch(i -> coincide(i, filtro));
    }

    private boolean coincide(String valor, String filtro) {
        return filtro == null || filtro.isBlank() || normalizar(valor).contains(normalizar(filtro));
    }

    private String normalizar(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "")
            .toLowerCase(Locale.ROOT)
            .trim();
    }
}

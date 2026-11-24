package cl.duoc.mesaabierta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class ApiJwtTest {
  @Autowired MockMvc mvc;
  @Test void apiPrivadaRechazaAnonimos() throws Exception {
    mvc.perform(get("/api/recetas/pasta-pesto")).andExpect(status().isUnauthorized());
    mvc.perform(post("/api/recetas").contentType("application/json").content("{}"))
      .andExpect(status().isUnauthorized());
  }
  @Test void loginInvalidoRechazado() throws Exception {
    mvc.perform(post("/api/auth/login").contentType("application/json")
      .content("{\"username\":\"chefana\",\"password\":\"incorrecta\"}"))
      .andExpect(status().isUnauthorized());
  }
  @Test void loginCorrectoEntregaJwt() throws Exception {
    mvc.perform(post("/api/auth/login").contentType("application/json")
      .content("{\"username\":\"chefana\",\"password\":\"Recetas2026!\"}"))
      .andExpect(status().isOk()).andExpect(jsonPath("$.accessToken").isNotEmpty())
      .andExpect(jsonPath("$.tokenType").value("Bearer"));
  }
  @Test void listadoEsPublico() throws Exception {
    mvc.perform(get("/api/recetas")).andExpect(status().isOk()).andExpect(jsonPath("$[0].slug").exists());
  }
}

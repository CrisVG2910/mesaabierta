package cl.duoc.mesaabierta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class PaginasSeguridadTest {
  @Autowired MockMvc mvc;
  @Test void homeBusquedaLoginPublicos() throws Exception {
    mvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("inicio"));
    mvc.perform(get("/buscar")).andExpect(status().isOk()).andExpect(view().name("buscar"));
    mvc.perform(get("/login")).andExpect(status().isOk());
  }
  @Test void detalleRequiereLogin() throws Exception {
    mvc.perform(get("/receta/pasta-pesto")).andExpect(status().is3xxRedirection());
  }
  @Test void paginaPrivadaCargaConUsuario() throws Exception {
    mvc.perform(get("/receta/pasta-pesto").with(user("chefana")))
      .andExpect(status().isOk()).andExpect(view().name("detalle"));
  }
  @Test void csrfSigueProtegiendoFormulariosWeb() throws Exception {
    mvc.perform(post("/logout").with(user("chefana"))).andExpect(status().isForbidden());
    mvc.perform(post("/logout").with(user("chefana")).with(csrf())).andExpect(status().is3xxRedirection());
  }
  @Test void filtroEscapaHtml() throws Exception {
    mvc.perform(get("/buscar").param("nombre","<svg onload=alert(1)>"))
      .andExpect(status().isOk())
      .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("value=\"<svg"))));
  }
}

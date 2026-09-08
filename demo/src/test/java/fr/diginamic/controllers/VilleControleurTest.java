package fr.diginamic.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.diginamic.dto.VilleDto;
import fr.diginamic.repository.DepartementRepository;
import fr.diginamic.repository.VilleRepository;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class VilleControleurTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VilleRepository villeRepository;

    @MockitoBean
    private DepartementRepository departementRepository;


    @Test
    public void testCreerVilleKo() throws Exception {

        VilleDto villeDto = new VilleDto();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/villes")
                                .content(objectMapper.writeValueAsString(villeDto))
                                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                                .accept(String.valueOf(MediaType.APPLICATION_JSON))
                )
                .andExpect(status().isBadRequest());
    }

}
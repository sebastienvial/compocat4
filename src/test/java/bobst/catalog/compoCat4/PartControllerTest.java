package bobst.catalog.compoCat4;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

@SpringBootTest
@AutoConfigureMockMvc
public class PartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // EndPoints test and result verification   
    @Test
    public void testGetDrawing() throws Exception {
        mockMvc.perform(get("/drawing/ZNC_BSA03802000066_017_-/BSA00340000MM"))
            .andExpect(status().isOk())
            .andExpect((ResultMatcher) jsonPath("$[0]", is("ZSP_BSA00340000MM_E43_017_-_P01")));
    }

    @Test
    public void testGetBomDynamique() throws Exception {
        mockMvc.perform(get("/bomd/ZNC_BSA03802000066_017_-/PCR0380E10"))
            .andExpect(status().isOk())
            .andExpect((ResultMatcher) jsonPath("$[0].item", is("PCR0380S1001")));
    }

    @Test
    public void testGetSearch() throws Exception {
        mockMvc.perform(get("/search/ZNC_BSA03802000066_017_-/joue"))
            .andExpect(status().isOk())
            .andExpect((ResultMatcher) jsonPath("$[0].id", is("BSA0115153800")));
    }

    @Test
    public void testGetPartsPage() throws Exception {
        mockMvc.perform(get("/catPartsPageContent/ZSP_BSA00340000MM_E43_017_-_P01"))
            .andExpect(status().isOk())
            .andExpect((ResultMatcher) jsonPath("$[0].numBobst", is("BSA01150000HG")));
    }

    @Test
    public void testGetDrawingE43() throws Exception {
        mockMvc.perform(get("/drawingsE43/ZSP_BSA00340000MM_E43_017_-"))
            .andExpect(status().isOk())
            .andExpect((ResultMatcher) jsonPath("$[0]", containsString("_P0")));
    }

    @Test
    public void testGetSVGDrawing() throws Exception {
        mockMvc.perform(get("/drawings/ZSP_BSA00340000MM_E43_017_-_P01.svg"))
            .andExpect(status().isOk());
    }


    
}



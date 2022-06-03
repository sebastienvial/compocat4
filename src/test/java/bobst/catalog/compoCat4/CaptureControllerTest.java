package bobst.catalog.compoCat4;

import bobst.catalog.compoCat4.controllers.CaptureController;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
//@ContextConfiguration(classes = WebConfig.class)
//@WebMvcTest(controllers = CaptureController.class)
public class CaptureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext wContext;

    @MockBean
    private  MultipartFile file;

    @Test
    public void testCaptureE43() throws Exception {
        
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wContext)
        .alwaysDo(MockMvcResultHandlers.print())
        .build();

        String test = "02.02.2022                                                                          Edition de liste dynamique                                                                                  1" +
        "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------" +
        "         Documents sélectionn.:    33.854" +
        "        -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------" +
        "        |  TéSupp|Ty.|Document                 |DPt|Vs|St|Description                             |MACHINE                       |TITRE SUPPLEMENTAIRE     |Du        |Au        |ELEMENT D'OTP         |"+
        "        -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"+
        "        |        |ZSP|BSA000059UCH_E43         |017|- |VL|MEC & ELE - RAMPES & DEFLECTEUR EJEC 65 |                              |                         |01.06.2008|02.02.2014|                      |"+
        "        |        |ZSP|BSA000059UCH_E43         |017|A |VL|MEC & ELE - RAMPES & DEFLECTEUR EJEC 65 |                              |                         |03.02.2014|31.12.9999|                      |"+
        "        |        |ZSP|BSA000061UCH_E43         |017|- |VL|MEC - BASE MODULE EJECTION 65 ---       |                              |                         |01.06.2008|01.06.2008|                      |";
        
        
        MockMultipartFile file = new MockMultipartFile("file", "helloE43.txt", MediaType.TEXT_PLAIN_VALUE, test.getBytes());

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/captureE43")       
                    .file(file) )               
            .andExpectAll(status().is(302)); // signifie redirection sur /capture => ok
            
    }


    @Test
    public void testCaptureXML() throws Exception {
        
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wContext)
        .alwaysDo(MockMvcResultHandlers.print())
        .build();

        String test = "<?xml version='1.0' encoding='utf-8' standalone='yes'?>";
        
        MockMultipartFile file = new MockMultipartFile("file", "test.xml", MediaType.TEXT_PLAIN_VALUE, test.getBytes());

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/captureXml")       
                    .file(file) )               
            .andExpectAll(status().is(302)); // signifie redirection sur /capture => ok
            
    }


    @Test
    public void testCaptureBom() throws Exception {
        
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wContext)
        .alwaysDo(MockMvcResultHandlers.print())
        .build();

        String test = "test BOM";
        
        MockMultipartFile file = new MockMultipartFile("file", "testBom.xt", MediaType.TEXT_PLAIN_VALUE, test.getBytes());

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/captureBom")       
                    .file(file) )               
            .andExpectAll(status().is(302)); // signifie redirection sur /capture => ok
            
    }


    @Test
    public void testCaptureZip() throws Exception {
        
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wContext)
        .alwaysDo(MockMvcResultHandlers.print())
        .build();

        String test = "test Zip";
        
        MockMultipartFile file = new MockMultipartFile("file", "test.zip", MediaType.TEXT_PLAIN_VALUE, test.getBytes());

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/captureZip")       
                    .file(file) )               
            .andExpectAll(status().is(302)); // signifie redirection sur /capture => ok
            
    }



}

package com.musala.soft.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.musala.soft.controllers.DroneController;
import com.musala.soft.models.dto.DroneDto;
import com.musala.soft.models.enums.Model;
import com.musala.soft.services.interfaces.DroneService;
import com.musala.soft.services.interfaces.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Random;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DroneController.class)
public class DroneValidationTest {

    @MockBean
    ProductService productService ;

    @MockBean
    DroneService droneService;

    @Autowired
    private MockMvc mockMvc;

    private final String UPPERCASE_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Test
    @DisplayName("Integration Test: Add Drones with wrong data")
    public void whenInputIsInvalid_thenReturnsStatus400() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/drones")
                .content(convertDroneDtoObjectToJsonString(initializeDroneObject(generateRandomString(3, UPPERCASE_ALPHABET), null, 0)))
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Integration Test: Serial Number bigger than 100 character")
    public void whenSerialNumberLengthBiggerThan100_thenReturnsStatus400() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/drones")
                .content(convertDroneDtoObjectToJsonString(initializeDroneObject(generateRandomString(101, UPPERCASE_ALPHABET), Model.Lightweight, 500.1)))
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Integration Test: Weight bigger than 100 character")
    public void whenWeightBiggerThan500_thenReturnsStatus400() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/drones")
                .content(convertDroneDtoObjectToJsonString(initializeDroneObject(generateRandomString(3, UPPERCASE_ALPHABET), Model.Lightweight, 500.1)))
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    DroneDto initializeDroneObject(String serialNumber, Model model, double weight){
        DroneDto droneDto = new DroneDto();
        droneDto.setSerialNumber(serialNumber);
        droneDto.setModel(model);
        droneDto.setWeight(weight);
        return droneDto;
    }

    String convertDroneDtoObjectToJsonString(DroneDto droneDto) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(droneDto);
    }

    String generateRandomString(int length, String alphabet) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}

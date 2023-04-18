package com.musala.soft.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.musala.soft.controllers.ProductController;
import com.musala.soft.models.dto.ProductDto;
import com.musala.soft.models.enums.Model;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Random;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProductController.class)
public class ProductValidationTest {
    @Autowired
    private MockMvc mockMvc;


    private final String LOWERCASE_LETTER = "abcdefghijklmnopqrstuvwxyz";
    private final String UPPERCASE_LETTER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String NUMBER = "0123456789";
    private final String UNDERSCORE_DASH = "_-";
    private final String SPECIAL_CHARACTER_STRING = ".+|@#$%*()";



    @Test
    public void whenNameHasSpecialCharacter_thenReturnsStatus400() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                .content(convertProductDtoObjectToJsonString(initializeDroneObject(generateRandomString(3, SPECIAL_CHARACTER_STRING), generateRandomString(3, UPPERCASE_LETTER+NUMBER+UNDERSCORE_DASH), 500.1)))
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenCodeHasSpecialCharacter_thenReturnsStatus400() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                .content(convertProductDtoObjectToJsonString(initializeDroneObject(generateRandomString(3, UPPERCASE_LETTER+NUMBER+LOWERCASE_LETTER+UNDERSCORE_DASH), generateRandomString(3, SPECIAL_CHARACTER_STRING), 500.1)))
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenCodeHasLowerCase_thenReturnsStatus400() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                .content(convertProductDtoObjectToJsonString(initializeDroneObject(generateRandomString(3, NUMBER+UPPERCASE_LETTER), generateRandomString(3, LOWERCASE_LETTER), 500.1)))
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }


    ProductDto initializeDroneObject(String name, String code, double weight){
        ProductDto productDto = new ProductDto();
        productDto.setName(name);
        productDto.setCode(code);
        productDto.setWeight(weight);
        return productDto;
    }

    String convertProductDtoObjectToJsonString(ProductDto productDto) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(productDto);
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

package com.lcwd.bridgelabz.addressbook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.bridgelabz.addressbook.dto.AddressBookDTO;
import com.lcwd.bridgelabz.addressbook.interfaces.IAddressBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressBookController.class)
@ExtendWith(MockitoExtension.class)
public class AddressBookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IAddressBookService addressBookService;

    private AddressBookDTO addressBookDTO;

    @BeforeEach
    void setUp() {
        addressBookDTO = new AddressBookDTO();
        addressBookDTO.setName("John Doe");
        addressBookDTO.setAddress("123 Main St, City");
        addressBookDTO.setPhoneNumber(1234567890L);
    }

    @Test
    void getAllAddressBook_ShouldReturnList() throws Exception {
        List<AddressBookDTO> addressList = List.of(addressBookDTO);
        Mockito.when(addressBookService.getAddressBookData()).thenReturn(addressList);

        mockMvc.perform(get("/api/addressbook").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].name").value("John Doe"));
    }

    @Test
    void getContactById_ShouldReturnAddressBook() throws Exception {
        Mockito.when(addressBookService.getAddressBookDataById(1L)).thenReturn(addressBookDTO);

        mockMvc.perform(get("/api/addressbook/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("John Doe"));
    }


    @Test
    void addInAddressBook_ShouldCreateNewContact() throws Exception {
        Mockito.when(addressBookService.createAddressBookData(any())).thenReturn(addressBookDTO);

        mockMvc.perform(post("/api/addressbook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressBookDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("John Doe"));
    }

    @Test
    void updateAddressBook_ShouldUpdateContact() throws Exception {
        Mockito.when(addressBookService.getAddressBookDataById(1L)).thenReturn(addressBookDTO);
        Mockito.when(addressBookService.updateAddressBookData(eq(1L), any())).thenReturn(true);

        mockMvc.perform(put("/api/addressbook/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressBookDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(containsString("Updated Employee Payroll Data")));
    }

    @Test
    void deleteAddressBook_ShouldDeleteContact() throws Exception {
        Mockito.when(addressBookService.getAddressBookDataById(1L)).thenReturn(addressBookDTO);
        Mockito.doNothing().when(addressBookService).deleteAddressBookData(1L);

        mockMvc.perform(delete("/api/addressbook/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(containsString("Deleted Employee Payroll Data")));
    }


}

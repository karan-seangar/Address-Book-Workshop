package com.lcwd.bridgelabz.addressbook.service;

import com.lcwd.bridgelabz.addressbook.dto.AddressBookDTO;
import com.lcwd.bridgelabz.addressbook.model.AddressBook;
import com.lcwd.bridgelabz.addressbook.repository.AddressBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressBookServiceTest {
    @Mock
    private AddressBookRepository addressBookRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AddressBookService addressBookService;

    private AddressBook sampleContact;
    private AddressBookDTO sampleDTO;

    @BeforeEach
    void setUp() {
        sampleContact = new AddressBook();
        sampleContact.setId(1L);
        sampleContact.setName("John Doe");
        sampleContact.setPhoneNumber(1234567890);
        sampleContact.setAddress("123 Main St");

        sampleDTO = new AddressBookDTO();
        sampleDTO.setName("John Doe");
        sampleDTO.setPhoneNumber(1234567890);
        sampleDTO.setAddress("123 Main St");
    }
    @Test
    void testGetAllContacts() {
        when(addressBookRepository.findAll()).thenReturn(List.of(sampleContact));

        List<AddressBookDTO> result = addressBookService.getAddressBookData();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.getFirst().getName());
    }

    @Test
    void testGetContactById() {
        when(addressBookRepository.findById(1L)).thenReturn(Optional.of(sampleContact));
        when(modelMapper.map(sampleContact, AddressBookDTO.class)).thenReturn(sampleDTO);

        AddressBookDTO result = addressBookService.getAddressBookDataById(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void testAddContact() {
        when(addressBookRepository.save(any(AddressBook.class))).thenReturn(sampleContact);
        when(modelMapper.map(sampleDTO, AddressBook.class)).thenReturn(sampleContact);
        when(modelMapper.map(sampleContact, AddressBookDTO.class)).thenReturn(sampleDTO);

        AddressBookDTO result = addressBookService.createAddressBookData(sampleDTO);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }


    @Test
    void testUpdateAddressBookData_Success() {
        // Mock repository to return an existing contact
        when(addressBookRepository.findById(1L)).thenReturn(Optional.of(sampleContact));

        // Mock modelMapper to update the object
        doNothing().when(modelMapper).map(sampleDTO, sampleContact);

        // Mock repository save method
        when(addressBookRepository.save(sampleContact)).thenReturn(sampleContact);

        // Call the service method
        boolean result = addressBookService.updateAddressBookData(1L, sampleDTO);

        // Assertions
        assertTrue(result, "Update should return true on success");
        verify(addressBookRepository, times(1)).save(sampleContact);
    }


    @Test
    void testUpdateAddressBookData_NotFound() {
        // Mock repository to return empty (simulating non-existent contact)
        when(addressBookRepository.findById(2L)).thenReturn(Optional.empty());

        // Call the service method
        boolean result = addressBookService.updateAddressBookData(2L, sampleDTO);

        // Assertions
        assertFalse(result, "Update should return false when the contact is not found");
        verify(addressBookRepository, never()).save(any(AddressBook.class));
    }


    @Test
    void testDeleteAddressBookData_Success() {
        // No need to mock findById() since deleteById() does not return anything

        // Call the delete method
        assertDoesNotThrow(() -> addressBookService.deleteAddressBookData(1L));

        // Verify that the repository deleteById() method was called once
        verify(addressBookRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteAddressBookData_Exception() {
        // Mock deleteById() to throw an exception
        doThrow(new RuntimeException("Address Book entry not found")).when(addressBookRepository).deleteById(2L);

        // Verify that the exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> addressBookService.deleteAddressBookData(2L));

        // Verify the message of the exception
        assertEquals("Employee Payroll not found with id: 2", exception.getMessage());

        // Verify deleteById() was attempted once
        verify(addressBookRepository, times(1)).deleteById(2L);
    }

}

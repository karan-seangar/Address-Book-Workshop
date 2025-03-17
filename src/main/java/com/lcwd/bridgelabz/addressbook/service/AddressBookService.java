package com.lcwd.bridgelabz.addressbook.service;

import com.lcwd.bridgelabz.addressbook.dto.AddressBookDTO;
import com.lcwd.bridgelabz.addressbook.interfaces.IAddressBookService;
import com.lcwd.bridgelabz.addressbook.model.AddressBook;
import com.lcwd.bridgelabz.addressbook.repository.AddressBookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookService implements IAddressBookService {

    @Autowired
    AddressBookRepository addressBookRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    @Cacheable(value = "addressBookCache")
    public List<AddressBookDTO> getAddressBookData() {
        List<AddressBook> addressBooksLists = addressBookRepository.findAll();
        return addressBooksLists.stream()
                .map(AddressBookDTO::new)
                .toList();
    }

    @Override
    @Cacheable(value = "addressBookCache", key = "#id")
    public AddressBookDTO getAddressBookDataById(long id) {
        AddressBook addressBook = addressBookRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee Payroll not found with id: " + id));
        return modelMapper.map(addressBook, AddressBookDTO.class);
    }

    @Override
    public AddressBookDTO createAddressBookData(AddressBookDTO addressBookDTO) {
        AddressBook addressBook = modelMapper.map(addressBookDTO, AddressBook.class);
        addressBook = addressBookRepository.save(addressBook);
        return modelMapper.map(addressBook, AddressBookDTO.class);
    }

    @Override
    public boolean updateAddressBookData(long id, AddressBookDTO updatedAddressBookDTO) {
        try {
            AddressBook addressBook = addressBookRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Address Book entry not found with id: " + id));
            modelMapper.map(updatedAddressBookDTO, addressBook);
            addressBookRepository.save(addressBook);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void deleteAddressBookData(long id) {
        try {
            addressBookRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Employee Payroll not found with id: " + id);
        }
    }
}

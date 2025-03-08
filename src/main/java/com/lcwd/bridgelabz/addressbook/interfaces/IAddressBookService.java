package com.lcwd.bridgelabz.addressbook.interfaces;

import com.lcwd.bridgelabz.addressbook.dto.AddressBookDTO;
import com.lcwd.bridgelabz.addressbook.model.AddressBook;

import java.util.List;

public interface IAddressBookService {
    List<AddressBookDTO> getAddressBookData();
    AddressBookDTO getAddressBookDataById(long id);
    AddressBookDTO createAddressBookData(AddressBookDTO empPayrollDTO);
    boolean updateAddressBookData(long id, AddressBookDTO updatedAddressBookDTO);
    void deleteAddressBookData(long id);
}

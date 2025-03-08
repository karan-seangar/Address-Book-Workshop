package com.lcwd.bridgelabz.addressbook.repository;

import com.lcwd.bridgelabz.addressbook.model.AddressBook;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AddressBookRepository extends JpaRepository<AddressBook, Long> {

}

package com.lcwd.bridgelabz.addressbook.dto;

import com.lcwd.bridgelabz.addressbook.model.AddressBook;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ADDRESS_BOOK")
public class AddressBookDTO {
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Address cannot be empty")
    @Size(min = 5, max = 100, message = "Address must be between 5 and 100 characters")
    private String address;

    @Digits(integer = 10, fraction = 0, message = "Phone number must be a 10-digit number")
    private long phoneNumber;

    public AddressBookDTO(AddressBook addressBook) {
        this.name = addressBook.getName();
        this.address = addressBook.getAddress();
        this.phoneNumber = addressBook.getPhoneNumber();
    }
}

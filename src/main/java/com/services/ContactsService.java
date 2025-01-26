package com.services;

import com.entities.Contacts;
import com.repositories.ContactsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactsService {
    @Autowired
    private ContactsRepo repo;

    public List<Contacts> getAllContacts() {
        return repo.findAll();
    }

    public Contacts getContactById(int id) {
        return repo.findById(id).get();
    }

    public Contacts addContact(Contacts contact) {
        return repo.save(contact);
    }

    public Contacts updateContact(Contacts contact) {
        //Contacts oldContact = repo.findById(contact.getId()).get();
        repo.deleteById(contact.getId());
        return repo.save(contact);
    }

    public void deleteContact(int id) {
        repo.deleteById(id);
    }

    public void deleteAllContacts() {
        repo.deleteAll();
    }
    public Contacts getContactsByName(String name) {
        Optional<Contacts> getContact = getAllContacts().stream().filter(contact -> contact.getPersonName().equals(name)).findFirst();
        return getContact.orElse(null);
    }

    public void addAllContacts(List<Contacts> contacts) {
        repo.saveAll(contacts);
    }
}

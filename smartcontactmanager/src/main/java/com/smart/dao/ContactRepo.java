package com.smart.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Contact;

public interface ContactRepo extends JpaRepository<Contact, Integer> {

    @Query("from Contact as c where c.user.id = :userid")
    public Page<Contact> findContactByuserID(@Param("userid") int id, Pageable pageable);
}

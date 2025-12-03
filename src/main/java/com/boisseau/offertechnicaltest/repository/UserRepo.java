package com.boisseau.offertechnicaltest.repository;

import com.boisseau.offertechnicaltest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    boolean existsByPhoneNumber(String phoneNumber);
}

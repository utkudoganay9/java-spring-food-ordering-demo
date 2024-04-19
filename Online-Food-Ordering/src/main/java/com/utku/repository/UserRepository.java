package com.utku.repository;

import com.utku.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
     User findByEmail(String username);

}

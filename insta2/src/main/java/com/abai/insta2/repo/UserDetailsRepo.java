package com.abai.insta2.repo;

import com.abai.insta2.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepo extends JpaRepository<User, String> {

}

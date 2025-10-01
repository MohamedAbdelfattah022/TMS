package com.mohamed.abdelfattah.tms.repositoris;

import com.mohamed.abdelfattah.tms.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
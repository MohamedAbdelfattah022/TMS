package com.mohamed.abdelfattah.tms.repositories;

import com.mohamed.abdelfattah.tms.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}

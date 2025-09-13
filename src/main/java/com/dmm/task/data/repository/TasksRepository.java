package com.dmm.task.data.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dmm.task.data.entity.Tasks;

public interface TasksRepository extends JpaRepository<Tasks, Integer> {
	 @Query("SELECT a FROM Tasks a WHERE a.date BETWEEN :from AND :to AND a.name = :name")
	    List<Tasks> findByDateBetween(
	        @Param("from") LocalDateTime from,
	        @Param("to") LocalDateTime to,
	        @Param("name") String name
	    );
	 @Query("SELECT a FROM Tasks a WHERE a.date BETWEEN :from AND :to")
	    List<Tasks> findAllByDateBetween(
	        @Param("from") LocalDateTime from,
	        @Param("to") LocalDateTime to
	    );
}
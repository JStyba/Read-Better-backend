package com.readbetter.main.repository;

import com.readbetter.main.model.Definition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefinitionRepository extends JpaRepository <Definition, Long> {
}

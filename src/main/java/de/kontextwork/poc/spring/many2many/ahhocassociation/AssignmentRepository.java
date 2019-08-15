package de.kontextwork.poc.spring.many2many.ahhocassociation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends
  JpaRepository<Assignment, AssignmentId>
{
}

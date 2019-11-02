package de.kontextwork.poc.spring.blaze.fullapp.subject;

import de.kontextwork.poc.spring.blaze.fullapp.subject.model.jpa.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long>
{
}

package de.kontextwork.poc.spring.blaze.subject.model.jpa;

import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "subject_subject")
@DiscriminatorColumn(name = "TYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class Subject
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(mappedBy = "subject", fetch = FetchType.LAZY)
  private RoleMember roleMember;
}

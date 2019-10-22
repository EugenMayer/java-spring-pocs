package de.kontextwork.poc.spring.blaze.subject.model.jpa.subject;

import de.kontextwork.poc.spring.blaze.subject.model.jpa.member.RoleMembership;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "subject_subject")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER)
public abstract class Subject
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(mappedBy = "subject", fetch = FetchType.LAZY)
  private RoleMembership roleMembership;
}
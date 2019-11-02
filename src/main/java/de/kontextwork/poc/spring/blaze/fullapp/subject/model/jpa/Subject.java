package de.kontextwork.poc.spring.blaze.fullapp.subject.model.jpa;

import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.GlobalRoleMembership;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.RealmRoleMembership;
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
  private GlobalRoleMembership globalRoleMembership;

  @OneToOne(mappedBy = "subject", fetch = FetchType.LAZY)
  private RealmRoleMembership realmRoleMembership;
}
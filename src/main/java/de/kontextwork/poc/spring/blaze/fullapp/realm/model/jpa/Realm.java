package de.kontextwork.poc.spring.blaze.fullapp.realm.model.jpa;

import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "subject_realm")
@NoArgsConstructor
public class Realm
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String realmName;

  @Size(max = 36)
  @Column(unique = true, nullable = false)
  private String machineName;

  private Realm(final String realmName, final String machineName)
  {
    this.realmName = realmName;
    this.machineName = machineName;
  }

  public Realm(final String realmName)
  {
    this(realmName, UUID.randomUUID().toString());
  }
}

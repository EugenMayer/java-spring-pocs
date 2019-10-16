package de.kontextwork.poc.spring.blaze.subject.model.jpa;

import javax.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@RequiredArgsConstructor
@Accessors(chain = true)
@Entity(name = "kontextwork_role_member")
public class RoleMember
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  private final Role role;

  @OneToOne(fetch = FetchType.LAZY)
  private final Subject subject;
}

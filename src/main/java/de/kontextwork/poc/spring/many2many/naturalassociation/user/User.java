package de.kontextwork.poc.spring.many2many.naturalassociation.user;

import javax.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "na_user_account")
public class User
{
  @Id
  @Column(length = 10)
  private String username;

  private String firstName;
  private String lastName;
}

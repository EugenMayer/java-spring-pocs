package de.kontextwork.poc.spring.many2many.naturalassociation.useraccount;

import javax.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "na_user_account")
public class UserAccount
{
  @Id
  @Column(length = 10)
  private String username;

  private String firstName;
  private String lastName;
}

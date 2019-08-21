package de.kontextwork.poc.spring.many2many.composite.user;

import javax.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "na_user")
public class User
{
  @Id
  @Column(length = 10)
  private String username;

  private String firstName;
  private String lastName;
}

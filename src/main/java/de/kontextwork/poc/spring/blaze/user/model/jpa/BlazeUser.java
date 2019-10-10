package de.kontextwork.poc.spring.blaze.user.model.jpa;

import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Table(name = "blaze_persistence_user")
@Data
@Entity
@Accessors(chain = true)
public class BlazeUser
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer uid;
  private String userName;
  private String firstName;
  private String lastName;
}
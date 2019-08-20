package de.kontextwork.poc.spring.many2many.naturalassociation.spacerole;

import de.kontextwork.poc.spring.many2many.naturalassociation.space.Space;
import de.kontextwork.poc.spring.many2many.naturalassociation.space.SpaceRepository;
import de.kontextwork.poc.spring.many2many.naturalassociation.user.User;
import de.kontextwork.poc.spring.many2many.naturalassociation.user.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

import static de.kontextwork.poc.spring.many2many.naturalassociation.spacerole.SpaceRoleRepository.Specifications.branchForSpace;
import static de.kontextwork.poc.spring.many2many.naturalassociation.spacerole.SpaceRoleRepository.Specifications.branchForUserAccount;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.data.jpa.domain.Specification.where;

@DataJpaTest
@SqlGroup({
  @Sql(scripts = "/sql/create-legacy-mappings.sql",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
  @Sql(scripts = "/sql/clear-legacy-mappings.sql",
    executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
class SpaceRoleRepositoryTest
{
  @Autowired
  private SpaceRepository spaceRepository;

  @Autowired
  private SpaceRoleRepository spaceRoleRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  @Order(0)
  @DisplayName("Should load SpaceRoles for Space 'Red'")
  public void shouldLoadSpaceRolesForSpaceBranch()
  {
    final Space red = spaceRepository.getOne("Red");
    final List<SpaceRole> redSpaceRoles = spaceRoleRepository.findAll(where(branchForSpace(red)));

    assertThat(redSpaceRoles.stream()
      .map(SpaceRole::getName)
      .collect(Collectors.toList()))
      .containsExactlyInAnyOrder("Admin", "Reader");
  }

  @Test
  @Order(1)
  @DisplayName("Should load SpaceRoles of Space 'Red' for 'sullrich'")
  public void shouldLoadSpaceRolesForUserAccount()
  {
    final Space red = spaceRepository.getOne("Red");
    final User sullrich = userRepository.getOne("sullrich");

    final List<SpaceRole> suSpaceRoles = spaceRoleRepository.findAll(
      where(branchForUserAccount(sullrich).and(branchForSpace(red))));

    // Be aware that this secs only apply on SpaceRole!
    // This is not a filtered sub graph where only sullrich is associated!
    assertThat(suSpaceRoles.stream()
      .map(SpaceRole::getName)
      .collect(Collectors.toList()))
      .containsExactlyInAnyOrder("Admin");
  }
}
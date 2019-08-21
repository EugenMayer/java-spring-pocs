package de.kontextwork.poc.spring.many2many.composite.spacerole;

import de.kontextwork.poc.spring.many2many.composite.space.Space;
import de.kontextwork.poc.spring.many2many.composite.space.SpaceRepository;
import de.kontextwork.poc.spring.many2many.composite.user.User;
import de.kontextwork.poc.spring.many2many.composite.user.UserRepository;
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

import static de.kontextwork.poc.spring.many2many.composite.spacerole.SpaceRoleRepository.Specifications.branchForSpace;
import static de.kontextwork.poc.spring.many2many.composite.spacerole.SpaceRoleRepository.Specifications.branchForUserAccount;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.data.jpa.domain.Specification.where;

@DataJpaTest
@SqlGroup({
  @Sql(scripts = "/sql/prefill-natural-association-data.sql",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
  @Sql(scripts = "/sql/clear-natural-association-data.sql.sql",
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
  @DisplayName("Should load SpaceRoles of Space 'Red' for 'sull'")
  public void shouldLoadSpaceRolesForUserAccount()
  {
    final Space red = spaceRepository.getOne("Red");
    final User sull = userRepository.getOne("sull");

    final List<SpaceRole> suSpaceRoles = spaceRoleRepository.findAll(
      where(branchForUserAccount(sull).and(branchForSpace(red))));

    // Be aware that this secs only apply on SpaceRole!
    // This is not a filtered sub graph where only sull is associated!
    assertThat(suSpaceRoles.stream()
      .map(SpaceRole::getName)
      .collect(Collectors.toList()))
      .containsExactlyInAnyOrder("Admin");
  }
}
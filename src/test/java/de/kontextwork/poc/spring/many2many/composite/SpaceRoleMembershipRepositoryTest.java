package de.kontextwork.poc.spring.many2many.composite;

import de.kontextwork.poc.spring.configuration.BlazePersistenceConfiguration;
import de.kontextwork.poc.spring.configuration.JpaConfiguration;
import de.kontextwork.poc.spring.many2many.composite.space.Space;
import de.kontextwork.poc.spring.many2many.composite.space.SpaceRepository;
import de.kontextwork.poc.spring.many2many.composite.spacerole.SpaceRole;
import de.kontextwork.poc.spring.many2many.composite.spacerole.SpaceRoleRepository;
import de.kontextwork.poc.spring.many2many.composite.user.User;
import de.kontextwork.poc.spring.many2many.composite.user.UserRepository;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@SqlGroup({
  @Sql(scripts = "/prefill-composite-data.sql",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
  @Sql(scripts = "/clear-composite-data.sql",
    executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
@Import(
  {
    JpaConfiguration.class,
    BlazePersistenceConfiguration.class
  }
)
class SpaceRoleMembershipRepositoryTest
{
  @Autowired
  private SpaceRepository spaceRepository;

  @Autowired
  private SpaceRoleRepository spaceRoleRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SpaceRoleMembershipRepository spaceRoleMembershipRepository;

  @Test
  @Order(0)
  @DisplayName("Should load test data from `prefill-natural-association-data.sql`")
  void shouldLoadTestData()
  {
    // Check if test data have been inserted
    assertEquals(5L, spaceRepository.count());
    assertEquals(3L, spaceRoleRepository.count());
    assertEquals(2L, userRepository.count());
    assertEquals(4L, spaceRoleMembershipRepository.count());
  }

  @Test
  @Order(1)
  @DisplayName("Should have Jan assigned as Admin for Space Green, Sebastian as Admin for Space Red")
  @Transactional
  public void shouldHaveAdminsAssigned()
  {
    assertThat(spaceRoleRepository.getOne("Admin").getSpaceRoleMemberships().stream()
      .map(SpaceRoleMembership::toString)
      .collect(Collectors.toSet()))
      .containsExactlyInAnyOrder("sull/Red/Admin", "jpre/Green/Admin");
  }

  @Test
  @Order(2)
  @DisplayName("Should only have Jan assigned as Reader for Space Red")
  @Transactional
  public void shouldHaveOnlyJanAssignedAsAuthor()
  {
    assertThat(spaceRoleRepository.getOne("Reader").getSpaceRoleMemberships().stream()
      .map(SpaceRoleMembership::toString)
      .collect(Collectors.toSet()))
      .containsExactly("jpre/Red/Reader");
  }

  @Test
  @Order(3)
  @DisplayName("Should only have Sebastian assigned as Author for Space Green")
  @Transactional
  public void shouldHaveSebastianAssignedAsReader()
  {
    assertThat(spaceRoleRepository.getOne("Author").getSpaceRoleMemberships().stream()
      .map(SpaceRoleMembership::toString)
      .collect(Collectors.toSet()))
      .containsExactly("sull/Green/Author");
  }

  @Test
  @Order(4)
  @DisplayName("Should persist new Space/Role assignment for new User")
  @Transactional
  public void shouldPersistNewSpaceRoleAssignmentForNewUserAccount()
  {
    User mMuster = userRepository.saveAndFlush(new User("mmuster", "Max", "Muster"));
    SpaceRole shinyRole = spaceRoleRepository.saveAndFlush(new SpaceRole("ShinyRole"));
    Space shinySpace = spaceRepository.saveAndFlush(new Space("ShinySpace"));

    SpaceRoleMembership musterPermission = new SpaceRoleMembership();
    musterPermission.setUser(mMuster);
    musterPermission.setSpaceRole(shinyRole);
    musterPermission.setSpace(shinySpace);

    spaceRoleMembershipRepository.saveAndFlush(musterPermission);

    // Reload Reader Role and check roleMemberships
    assertThat(spaceRoleRepository.getOne("ShinyRole").getSpaceRoleMemberships().stream()
      .map(SpaceRoleMembership::toString)
      .collect(Collectors.toSet()))
      .containsExactlyInAnyOrder("mmuster/ShinySpace/ShinyRole");
  }
}
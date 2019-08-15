package de.kontextwork.poc.spring.many2many.naturalassociation.spacerole;

import de.kontextwork.poc.spring.many2many.naturalassociation.LegacyMapping;
import de.kontextwork.poc.spring.many2many.naturalassociation.LegacyMappingRepository;
import de.kontextwork.poc.spring.many2many.naturalassociation.space.Space;
import de.kontextwork.poc.spring.many2many.naturalassociation.space.SpaceRepository;
import de.kontextwork.poc.spring.many2many.naturalassociation.useraccount.UserAccount;
import de.kontextwork.poc.spring.many2many.naturalassociation.useraccount.UserAccountRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.jdbc.Sql;

import static de.kontextwork.poc.spring.many2many.naturalassociation.spacerole.SpaceRoleRepository.Specifications.branchForSpace;
import static de.kontextwork.poc.spring.many2many.naturalassociation.spacerole.SpaceRoleRepository.Specifications.branchForUserAccount;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.data.jpa.domain.Specification.where;

@DataJpaTest
@Sql("/sql/create-legacy-mappings.sql")
class SpaceRoleRepositoryTest
{
  @Autowired
  private SpaceRepository spaceRepository;

  @Autowired
  private SpaceRoleRepository spaceRoleRepository;

  @Autowired
  private UserAccountRepository userAccountRepository;

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
    final UserAccount sullrich = userAccountRepository.getOne("sullrich");

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
package de.kontextwork.poc.spring.many2many.naturalassociation;

import de.kontextwork.poc.spring.many2many.naturalassociation.space.Space;
import de.kontextwork.poc.spring.many2many.naturalassociation.space.SpaceRepository;
import de.kontextwork.poc.spring.many2many.naturalassociation.spacerole.SpaceRole;
import de.kontextwork.poc.spring.many2many.naturalassociation.spacerole.SpaceRoleRepository;
import de.kontextwork.poc.spring.many2many.naturalassociation.useraccount.UserAccount;
import de.kontextwork.poc.spring.many2many.naturalassociation.useraccount.UserAccountRepository;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql("/sql/create-legacy-mappings.sql")
class LegacyMappingRepositoryTest
{
  @Autowired
  private SpaceRepository spaceRepository;

  @Autowired
  private SpaceRoleRepository spaceRoleRepository;

  @Autowired
  private UserAccountRepository userAccountRepository;

  @Autowired
  private LegacyMappingRepository legacyMappingRepository;

  @Test
  @Order(0)
  @DisplayName("Should load test data from `create-legacy-mappings.sql`")
  public void shouldLoadTestData()
  {
    // Check if test data have been inserted
    assertEquals(5L, spaceRepository.count());
    assertEquals(3L, spaceRoleRepository.count());
    assertEquals(2L, userAccountRepository.count());
    assertEquals(4L, legacyMappingRepository.count());
  }

  @Test
  @Order(1)
  @DisplayName("Should have Jan assigned as Admin for Space Green, Sebastian as Admin for Space Red")
  public void shouldHaveAdminsAssigned()
  {
    assertThat(spaceRoleRepository.getOne("Admin").getLegacyMappings().stream()
      .map(LegacyMapping::toString)
      .collect(Collectors.toList()))
      .containsExactlyInAnyOrder("sullrich/Red/Admin", "jpretzel/Green/Admin");
  }

  @Test
  @Order(2)
  @DisplayName("Should only have Jan assigned as Reader for Space Red")
  public void shouldHaveOnlyJanAssignedAsAuthor()
  {
    assertThat(spaceRoleRepository.getOne("Reader").getLegacyMappings().stream()
      .map(LegacyMapping::toString)
      .collect(Collectors.toList()))
      .containsExactly("jpretzel/Red/Reader");
  }

  @Test
  @Order(3)
  @DisplayName("Should only have Sebastian assigned as Author for Space Green")
  public void shouldHaveSebastianAssignedAsReader()
  {
    assertThat(spaceRoleRepository.getOne("Author").getLegacyMappings().stream()
      .map(LegacyMapping::toString)
      .collect(Collectors.toList()))
      .containsExactly("sullrich/Green/Author");
  }

  @Test
  @Order(4)
  @DisplayName("Should persist new Space/Role assignment for new UserAccount")
  public void shouldPersistNewSpaceRoleAssignmentForNewUserAccount()
  {
    UserAccount mMuster = userAccountRepository.save(new UserAccount("mmuster", "Max", "Muster"));
    SpaceRole reader = spaceRoleRepository.getOne("Reader");
    Space black = spaceRepository.getOne("Black");

    LegacyMapping musterPermission = LegacyMapping.builder()
      .userAccount(mMuster)
      .spaceRole(reader)
      .space(black)
      .build();

    legacyMappingRepository.saveAndFlush(musterPermission);

    // Reload Reader Role and check assignments
    assertThat(spaceRoleRepository.getOne("Reader").getLegacyMappings().stream()
      .map(LegacyMapping::toString)
      .collect(Collectors.toList()))
      .containsExactlyInAnyOrder("jpretzel/Red/Reader", "mmuster/Black/Reader");
  }
}
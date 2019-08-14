package de.kontextwork.poc.spring.many2many.compositepk;

import de.kontextwork.poc.spring.many2many.compositepk.company.Company;
import de.kontextwork.poc.spring.many2many.compositepk.company.CompanyRepository;
import de.kontextwork.poc.spring.many2many.compositepk.role.Role;
import de.kontextwork.poc.spring.many2many.compositepk.role.RoleRepository;
import de.kontextwork.poc.spring.many2many.compositepk.user.User;
import de.kontextwork.poc.spring.many2many.compositepk.user.UserRepository;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AssignmentTest
{
  @Autowired
  private CompanyRepository companyRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AssignmentRepository assignmentRepository;

  @Autowired
  private EntityManager entityManager;

  @Test
  @Transactional
  @DisplayName("Should generate Test Data")
  public void shouldGenerateTestData()
  {
    // Create Apple Realm
    final Company apple = Company.builder().name("Apple").build();
    final User steveJobs = User.builder().firstName("Steve").lastName("Jobs").build();
    final User steveWozniak = User.builder().firstName("Steve").lastName("Wozniak").build();

    companyRepository.saveAndFlush(apple);
    userRepository.saveAndFlush(steveJobs);
    userRepository.saveAndFlush(steveWozniak);

    // Create Microsoft Realm
    final Company microsoft = Company.builder().name("Microsoft").build();
    final User billGates = User.builder().firstName("Bill").lastName("Gates").build();
    final User adamNathan = User.builder().firstName("Adam").lastName("Nathan").build();

    companyRepository.saveAndFlush(microsoft);
    userRepository.saveAndFlush(billGates);
    userRepository.saveAndFlush(adamNathan);

    // Create Facebook Realm
    final Company facebook = Company.builder().name("Facebook").build();
    final User markZuckerberg = User.builder().firstName("Mark").lastName("Zuckerberg").build();
    final User danAbramov = User.builder().firstName("Dan").lastName("Abramov").build();

    companyRepository.saveAndFlush(facebook);
    userRepository.saveAndFlush(markZuckerberg);
    userRepository.saveAndFlush(danAbramov);

    // Create Roles
    final Role ceo = Role.builder().name("Chief Executive Officer").build();
    final Role employee = Role.builder().name("Employee").build();

    roleRepository.saveAndFlush(ceo);
    roleRepository.saveAndFlush(employee);

    // Pre check entries
    assertEquals(2L, roleRepository.count(), "Should have two roles persisted");
    assertEquals(3L, companyRepository.count(), "Should have three groups persisted");
    assertEquals(6L, userRepository.count(), "Should have six users persisted");

    // Create entries which emulates our legacy table
    assignmentRepository.saveAll(List.of(
      createAssignment(apple, steveJobs, ceo),
      createAssignment(apple, steveWozniak, employee),

      createAssignment(microsoft, billGates, ceo),
      createAssignment(microsoft, adamNathan, employee),

      createAssignment(facebook, markZuckerberg, ceo),
      createAssignment(facebook, danAbramov, employee)
    ));
    assignmentRepository.flush();
    assertEquals(6L, assignmentRepository.count(), "Should have six assignments persisted");

    // Clear Hibernates L1 cache
    entityManager.clear();

    final List<Role> roles = roleRepository.findAll();
    assertEquals(2L, roles.size());
    assertEquals(3L, roles.get(0).getMembers().size());
    assertEquals(3L, roles.get(1).getMembers().size());
  }

  /**
   * This will create entries for our `legacy` association table.
   *
   * @param company {@link Company} relation
   * @param user {@link User} relation
   * @param role {@link Role} relation
   */
  private Assignment createAssignment(Company company, User user, Role role)
  {
    AssignmentId compositeId = AssignmentId.builder()
      .companyId(company.getId())
      .userId(user.getId())
      .roleId(role.getId())
      .build();

    return Assignment.builder()
      .assignmentId(compositeId)
      .build();
  }
}
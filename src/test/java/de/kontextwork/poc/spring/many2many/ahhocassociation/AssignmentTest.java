package de.kontextwork.poc.spring.many2many.ahhocassociation;

import de.kontextwork.poc.spring.many2many.ahhocassociation.company.Company;
import de.kontextwork.poc.spring.many2many.ahhocassociation.company.CompanyRepository;
import de.kontextwork.poc.spring.many2many.ahhocassociation.role.Role;
import de.kontextwork.poc.spring.many2many.ahhocassociation.role.RoleRepository;
import de.kontextwork.poc.spring.many2many.ahhocassociation.person.Person;
import de.kontextwork.poc.spring.many2many.ahhocassociation.person.PersonRepository;
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
  private PersonRepository personRepository;

  @Autowired
  private AssignmentRepository assignmentRepository;

  @Autowired
  private EntityManager entityManager;

  @Test
  @Transactional
  @DisplayName("Should generate Test Data")
  public void shouldGenerateTestData()
  {
    // Create Apple Space
    final Company apple = Company.builder().name("Apple").build();
    final Person steveJobs = Person.builder().firstName("Steve").lastName("Jobs").build();
    final Person steveWozniak = Person.builder().firstName("Steve").lastName("Wozniak").build();

    companyRepository.saveAndFlush(apple);
    personRepository.saveAndFlush(steveJobs);
    personRepository.saveAndFlush(steveWozniak);

    // Create Microsoft Space
    final Company microsoft = Company.builder().name("Microsoft").build();
    final Person billGates = Person.builder().firstName("Bill").lastName("Gates").build();
    final Person adamNathan = Person.builder().firstName("Adam").lastName("Nathan").build();

    companyRepository.saveAndFlush(microsoft);
    personRepository.saveAndFlush(billGates);
    personRepository.saveAndFlush(adamNathan);

    // Create Facebook Space
    final Company facebook = Company.builder().name("Facebook").build();
    final Person markZuckerberg = Person.builder().firstName("Mark").lastName("Zuckerberg").build();
    final Person danAbramov = Person.builder().firstName("Dan").lastName("Abramov").build();

    companyRepository.saveAndFlush(facebook);
    personRepository.saveAndFlush(markZuckerberg);
    personRepository.saveAndFlush(danAbramov);

    // Create Roles
    final Role ceo = Role.builder().name("Chief Executive Officer").build();
    final Role employee = Role.builder().name("Employee").build();

    roleRepository.saveAndFlush(ceo);
    roleRepository.saveAndFlush(employee);

    // Pre check entries
    assertEquals(2L, roleRepository.count(), "Should have two roles persisted");
    assertEquals(3L, companyRepository.count(), "Should have three groups persisted");
    assertEquals(6L, personRepository.count(), "Should have six users persisted");

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

    final List<Role> roles = roleRepository.findAll();
    assertEquals(2L, roles.size());

    // Clear Hibernates L1 cache
    // This needs to be done, otherwise roles.get(0).getMembers() is NULL, since the adhoc relation is not updated
    // and somehow the L1 cache is yet not valid
    // FIXME: This is most probably due to the fact, that we have no bi-directional relation between the assigment
    //  and the role, so hibernate does not understand what is actually mapped to it and does not update the L1 cache
    entityManager.clear();
    assertEquals(3L, roles.get(0).getMembers().size());
    assertEquals(3L, roles.get(1).getMembers().size());
  }

  /**
   * This will create entries for our `legacy` association table.
   *
   * @param company {@link Company} relation
   * @param person {@link Person} relation
   * @param role {@link Role} relation
   */
  private Assignment createAssignment(Company company, Person person, Role role)
  {
    AssignmentId compositeId = AssignmentId.builder()
      .companyId(company.getId())
      .personId(person.getId())
      .roleId(role.getId())
      .build();

    return Assignment.builder()
      .assignmentId(compositeId)
      .build();
  }
}
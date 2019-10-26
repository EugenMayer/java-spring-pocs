package de.kontextwork.poc.spring.blaze.subject;

import com.github.javafaker.Faker;
import de.kontextwork.poc.spring.blaze.core.PageableEntityViewRepository;
import de.kontextwork.poc.spring.blaze.core.RegularEntityViewRepository;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.User;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.member.GlobalRoleMembership;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.privilege.*;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.role.GlobalRole;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.subject.Group;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.subject.Subject;
import de.kontextwork.poc.spring.configuration.BlazePersistenceConfiguration;
import de.kontextwork.poc.spring.configuration.JpaBlazeConfiguration;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.assertj.core.api.Assertions.assertThat;

@Import({
  SubjectService.class,
  JpaBlazeConfiguration.class,
  BlazePersistenceConfiguration.class,
  PageableEntityViewRepository.class,
  RegularEntityViewRepository.class
})
@AutoConfigureDataJdbc
@TestMethodOrder(OrderAnnotation.class)
@DataJpaTest(properties = {"blazepersistance.enabled=true"}, showSql = false)
public class BenchmarkTesting
{
  private final Faker faker = new Faker();

  private final int BENCHMARK_ITERATIONS = 1000;

  @Autowired
  private SubjectService subjectService;

  @Autowired
  private SubjectRepository subjectRepository;

  @Autowired
  public PrivilegeRepository privilegeRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  public GlobalRoleMembershipRepository globalRoleMembershipRepository;

  private static Subject testSubject;
  private static String testPrivilege;

  @Test
  @Order(1)
  @DisplayName("Create Test Scenario")
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  public void createTestScenario()
  {
    Supplier<String> randomString = () -> UUID.randomUUID().toString();
    Supplier<User> randomUser = () -> new User(randomString.get(), randomString.get());
    Supplier<GlobalPrivilege> randomPrivilege = () -> new GlobalPrivilege(randomString.get());

    Function<Set<User>, Group> randomGroup = u -> new Group(randomString.get(), u);
    Function<Set<GlobalPrivilege>, GlobalRole> randomRole = p -> new GlobalRole(randomString.get(), p);

    // Create 450 Group Users
    List<User> groupUsers = subjectRepository.saveAll(IntStream.range(0, 450)
      .mapToObj(i -> randomUser.get())
      .collect(Collectors.toSet()));

    // Create 30 Groups containing 25 Users (450 Users total)
    List<Group> groups = subjectRepository.saveAll(IntStream.range(0, 30)
      .mapToObj(i -> randomGroup.apply(new HashSet<>(groupUsers.subList(i * 15, i * 15 + 15))))
      .collect(Collectors.toList()));

    // Create 50 users without Group
    List<User> users = subjectRepository.saveAll(IntStream.range(0, 50)
      .mapToObj(i -> randomUser.get())
      .collect(Collectors.toList()));

    // Create 125 Privileges
    List<GlobalPrivilege> privileges = privilegeRepository.saveAll(IntStream.range(0, 125)
      .mapToObj(i -> randomPrivilege.get())
      .collect(Collectors.toSet()));

    // Create 25 Roles containing (5 Privileges)
    List<GlobalRole> roles = roleRepository.saveAll(IntStream.range(0, 25)
      .mapToObj(i -> randomRole.apply(new HashSet<>(privileges.subList(i * 5, i * 5 + 5))))
      .collect(Collectors.toList()));

    // Create 50 user role assignments
    globalRoleMembershipRepository.saveAll(IntStream.range(0, 50)
      .mapToObj(i -> new GlobalRoleMembership(roles.get(i / 2), users.get(i)))
      .collect(Collectors.toList()));

    // Create 450 group user role assignment
    globalRoleMembershipRepository.saveAll(IntStream.range(0, 450)
      .mapToObj(i -> new GlobalRoleMembership(roles.get(i / 18), groupUsers.get(i)))
      .collect(Collectors.toList()));

    // Create 300 random group role assignments
    Set<GlobalRoleMembership> groupRoleMemberships = new HashSet<>();
    while (groupRoleMemberships.size() < 300) {
      int roleIdx = faker.random().nextInt(0, roles.size() - 1);
      int groupIdx = faker.random().nextInt(0, groups.size() - 1);
      groupRoleMemberships.add(new GlobalRoleMembership(roles.get(roleIdx), groups.get(groupIdx)));
    }
    globalRoleMembershipRepository.saveAll(groupRoleMemberships);

    assertThat(subjectRepository.count()).isEqualTo(530);
    assertThat(roleRepository.count()).isEqualTo(25);
    assertThat(privilegeRepository.count()).isEqualTo(125);
    assertThat(globalRoleMembershipRepository.count()).isEqualTo(800);

    // Pick random subject and privilege for benchmarking
    List<Subject> subjects = new LinkedList<>();
    subjects.addAll(groups);
    subjects.addAll(groupUsers);
    subjects.addAll(users);
    testSubject = subjects.get(faker.random().nextInt(0, subjects.size() - 1));
    testPrivilege = privileges.get(faker.random().nextInt(0, privileges.size() - 1)).getName();

    assertThat(testSubject.getId()).isNotNull();
    assertThat(testPrivilege).isNotNull();
  }

  @Order(2)
  @RepeatedTest(BENCHMARK_ITERATIONS)
  @DisplayName("Benchmark Privilege Check from Subject")
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void benchmarkPrivilegeCheckFromSubject()
  {
    assertThat(subjectService.hasSubjectPrivilegeFromSubject(testSubject, testPrivilege)).isNotNull();
  }

  @Order(3)
  @RepeatedTest(BENCHMARK_ITERATIONS)
  @DisplayName("Benchmark Privilege Check from Privilege")
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void benchmarkPrivilegeCheckFromPrivilege()
  {
    assertThat(subjectService.hasSubjectPrivilegeFromPrivilege(testSubject, testPrivilege)).isNotNull();
  }

  @Order(4)
  @RepeatedTest(BENCHMARK_ITERATIONS)
  @DisplayName("Benchmark Privilege Check from Role Membership")
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void benchmarkPrivilegeCheckFromRoleMembership()
  {
    assertThat(subjectService.hasSubjectPrivilegeFromGlobalRoleMembership(testSubject, testPrivilege)).isNotNull();
  }
}

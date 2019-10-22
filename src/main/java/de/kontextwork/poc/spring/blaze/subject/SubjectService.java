package de.kontextwork.poc.spring.blaze.subject;

import com.blazebit.persistence.*;
import com.blazebit.persistence.view.EntityViewSetting;
import de.kontextwork.poc.spring.blaze.core.PageableEntityViewRepository;
import de.kontextwork.poc.spring.blaze.core.RegularEntityViewRepository;
import de.kontextwork.poc.spring.blaze.subject.model.RealmRoleMemberRepository;
import de.kontextwork.poc.spring.blaze.subject.model.domain.*;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.*;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.member.*;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.privilege.*;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.role.RealmRole;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.role.Role;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.subject.*;
import java.util.Set;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubjectService
{
  private final EntityManager entityManager;
  private final RoleRepository roleRepository;
  private final RealmRepository realmRepository;
  private final SubjectRepository subjectRepository;
  private final PrivilegeRepository privilegeRepository;
  private final RoleMemberRepository roleMemberRepository;
  private final CriteriaBuilderFactory criteriaBuilderFactory;
  private final RealmRoleMemberRepository realmRoleMemberRepository;
  private final PageableEntityViewRepository<User> userViewRepository;
  private final PageableEntityViewRepository<Group> groupViewRepository;
  private final PageableEntityViewRepository<Subject> subjectViewRepository;
  private final RegularEntityViewRepository<Subject, Long> regularEntityViewRepository;

  /**
   * Creates new {@link User}.
   */
  public <S extends Subject> S create(final S user)
  {
    return subjectRepository.save(user);
  }

  /**
   * Creates new {@link Role}.
   */
  public <R extends Role> R create(final R role)
  {
    return roleRepository.save(role);
  }

  /**
   * Creates new {@link Realm}.
   */
  public Realm create(final Realm realm)
  {
    return realmRepository.save(realm);
  }

  /**
   * Creates new {@link Privilege}.
   */
  public <P extends Privilege> P create(final P privilege)
  {
    return privilegeRepository.save(privilege);
  }

  /**
   * Creates new {@link GlobalRoleMembership} from provided {@code role} and {@code subject}.
   */
  public void assign(final Role role, final Subject subject)
  {
    roleMemberRepository.save(new GlobalRoleMembership(role, subject));
  }

  /**
   * Creates new {@link RealmRoleMembership} from provided {@code realm}, {@code role} and {@code subject}.
   */
  public void assign(final Realm realm, final RealmRole role, final Subject subject)
  {
    realmRoleMemberRepository.save(new RealmRoleMembership(realm, role, subject));
  }

  /**
   * @return {@link Page} of {@link SubjectView} matching provided {@code setting}.
   */
  public Page<SubjectView> getSubjects(
    EntityViewSetting<SubjectView, PaginatedCriteriaBuilder<SubjectView>> setting, Pageable pageable
  )
  {
    CriteriaBuilder<Subject> criteriaBuilder = criteriaBuilderFactory.create(entityManager, Subject.class);
    return subjectViewRepository.findAll(setting, criteriaBuilder, pageable);
  }

  /**
   * @return {@link Set} of {@link SubjectView} matching provided {@code setting}.
   */
  public Set<SubjectView> getSubjects(
    EntityViewSetting<SubjectView, CriteriaBuilder<SubjectView>> setting
  )
  {
    CriteriaBuilder<Subject> criteriaBuilder = regularEntityViewRepository.entityCriteriaBuilder(Subject.class);
    return regularEntityViewRepository.findAll(setting, criteriaBuilder);
  }

  /**
   * @return {@link Page} of {@link SubjectUserView} matching provided {@code setting}.
   */
  public Page<SubjectUserView> getSubjectUsers(
    EntityViewSetting<SubjectUserView, PaginatedCriteriaBuilder<SubjectUserView>> setting, Pageable pageable
  )
  {
    CriteriaBuilder<User> criteriaBuilder = criteriaBuilderFactory.create(entityManager, User.class);
    return userViewRepository.findAll(setting, criteriaBuilder, pageable);
  }

  /**
   * @return {@link Page} of {@link SubjectGroupView} matching provided {@code setting}.
   */
  public Page<SubjectGroupView> getSubjectGroups(
    EntityViewSetting<SubjectGroupView, PaginatedCriteriaBuilder<SubjectGroupView>> setting, Pageable pageable
  )
  {
    CriteriaBuilder<Group> criteriaBuilder = criteriaBuilderFactory.create(entityManager, Group.class);
    return groupViewRepository.findAll(setting, criteriaBuilder, pageable);
  }
}

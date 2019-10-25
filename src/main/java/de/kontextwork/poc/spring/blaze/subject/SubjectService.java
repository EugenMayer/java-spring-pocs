package de.kontextwork.poc.spring.blaze.subject;

import com.blazebit.persistence.*;
import com.blazebit.persistence.view.EntityViewSetting;
import de.kontextwork.poc.spring.blaze.core.PageableEntityViewRepository;
import de.kontextwork.poc.spring.blaze.core.RegularEntityViewRepository;
import de.kontextwork.poc.spring.blaze.subject.model.domain.*;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.*;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.member.*;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.privilege.*;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.role.RealmRole;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.role.Role;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.subject.*;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class SubjectService
{
  private final EntityManager entityManager;
  private final RoleRepository roleRepository;
  private final RealmRepository realmRepository;
  private final SubjectRepository subjectRepository;
  private final PrivilegeRepository privilegeRepository;
  private final CriteriaBuilderFactory criteriaBuilderFactory;
  private final PageableEntityViewRepository<User> userPageableViewRepository;
  private final PageableEntityViewRepository<Group> groupPageableViewRepository;
  private final PageableEntityViewRepository<Subject> subjectPageableViewRepository;
  private final RealmRoleMembershipRepository realmRoleMembershipRepository;
  private final GlobalRoleMembershipRepository globalRoleMembershipRepository;
  private final RegularEntityViewRepository<Subject, Long> subjectViewRepository;
  private final RegularEntityViewRepository<Privilege, Long> privilegeViewRepository;

  /**
   * Creates new {@link User}.
   */
  public <S extends Subject> S create(final S subject)
  {
    return subjectRepository.save(subject);
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
    globalRoleMembershipRepository.save(new GlobalRoleMembership(role, subject));
  }

  /**
   * Creates new {@link RealmRoleMembership} from provided {@code realm}, {@code role} and {@code subject}.
   */
  public void assign(final Realm realm, final RealmRole role, final Subject subject)
  {
    realmRoleMembershipRepository.save(new RealmRoleMembership(realm, role, subject));
  }

  /**
   * @return {@link Page} of {@link SubjectView} matching provided {@code setting}.
   */
  public Page<SubjectView> getSubjects(
    EntityViewSetting<SubjectView, PaginatedCriteriaBuilder<SubjectView>> setting, Pageable pageable
  )
  {
    CriteriaBuilder<Subject> criteriaBuilder = criteriaBuilderFactory.create(entityManager, Subject.class);
    return subjectPageableViewRepository.findAll(setting, criteriaBuilder, pageable);
  }

  /**
   * @return {@link Set} of {@link SubjectView} matching provided {@code setting}.
   */
  public Set<SubjectView> getSubjects(EntityViewSetting<SubjectView, CriteriaBuilder<SubjectView>> setting)
  {
    CriteriaBuilder<Subject> criteriaBuilder = subjectViewRepository.entityCriteriaBuilder(Subject.class);
    return subjectViewRepository.findAll(setting, criteriaBuilder);
  }

  /**
   * @return {@link Page} of {@link SubjectUserView} matching provided {@code setting}.
   */
  public Page<SubjectUserView> getSubjectUsers(
    EntityViewSetting<SubjectUserView, PaginatedCriteriaBuilder<SubjectUserView>> setting, Pageable pageable
  )
  {
    CriteriaBuilder<User> criteriaBuilder = criteriaBuilderFactory.create(entityManager, User.class);
    return userPageableViewRepository.findAll(setting, criteriaBuilder, pageable);
  }

  /**
   * @return {@link Page} of {@link SubjectGroupView} matching provided {@code setting}.
   */
  public Page<SubjectGroupView> getSubjectGroups(
    EntityViewSetting<SubjectGroupView, PaginatedCriteriaBuilder<SubjectGroupView>> setting, Pageable pageable
  )
  {
    CriteriaBuilder<Group> criteriaBuilder = criteriaBuilderFactory.create(entityManager, Group.class);
    return groupPageableViewRepository.findAll(setting, criteriaBuilder, pageable);
  }

  public boolean hasSubjectPrivilege_fromPrivilege(Subject subject, String privilege)
  {
    CriteriaBuilder<Integer> criteria = criteriaBuilderFactory.create(entityManager, Integer.class)
      .from(Privilege.class, "privilege")
      .select("1")
      .where("privilege.name").eq(privilege);

    // We have to handle users differently since user can inherit privileges by their groups
    if (subject instanceof User) {
      // Select all groups where user is member of
      var groupIds = criteriaBuilderFactory.create(entityManager, String.class)
        .from(Group.class, "grp").select("id")
        .where("grp.members.id").eq(subject.getId())
        .getQuery();

      criteria.where("privilege.roles.globalRoleMembership.subject.id").in(groupIds);
    }
    else {
      criteria.where("privilege.roles.globalRoleMembership.subject.id").eq(subject.getId());
    }

    return !criteria.setMaxResults(1).getResultList().isEmpty(); // Expression is negated!!!
  }

  public boolean hasSubjectPrivilege_fromGlobalRoleMembership(Subject subject, String privilege)
  {
    return criteriaBuilderFactory.create(entityManager, Integer.class)
      .from(GlobalRoleMembership.class, "globalRoleMembership")
      .select("1")
      .where("globalRoleMembership.subject.id").eq(subject.getId())
      .where("globalRoleMembership.role.privileges.name").eq(privilege)
      .setMaxResults(1)
      .getResultList()
      .size() > 0;
  }

  public boolean hasSubjectPrivilege_fromSubject(Subject subject, String privilege)
  {
    return !criteriaBuilderFactory.create(entityManager, Integer.class)
      .from(Subject.class, "subject")
      .select("1")
      .where("subject.id").eq(subject.getId())
      .where("subject.globalRoleMembership.role.privileges.name").eq(privilege)
      .setMaxResults(1)
      .getResultList()
      .isEmpty(); // Expression is negated!!!
  }
}

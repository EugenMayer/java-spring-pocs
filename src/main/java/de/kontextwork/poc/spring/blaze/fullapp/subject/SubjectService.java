package de.kontextwork.poc.spring.blaze.fullapp.subject;

import com.blazebit.persistence.*;
import com.blazebit.persistence.view.EntityViewSetting;
import de.kontextwork.poc.spring.blaze.core.*;
import de.kontextwork.poc.spring.blaze.fullapp.privilege.model.jpa.Privilege;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.GlobalRoleMembership;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.jpa.Group;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.jpa.Subject;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.jpa.User;
import java.util.Optional;
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
  private final SubjectRepository subjectRepository;
  private final CriteriaBuilderFactory criteriaBuilderFactory;
  private final PageableEntityViewRepository<Subject> subjectPageableViewRepository;
  private final RegularEntityViewRepository<Subject, Long> subjectViewRepository;
  /**
   * Creates new {@link User}.
   */
  public <S extends Subject> S create(final S subject)
  {
    return subjectRepository.save(subject);
  }

  public Optional<SubjectIdView> getOneAsIdView(Long id) {
    return subjectViewRepository.getOne(EntityViewSetting.create(SubjectIdView.class), id);
  }

  /**
   * @return {@link Page} of {@link SubjectView} matching provided {@code setting}.
   */
  public Page<SubjectView> findAll(
    EntityViewSetting<SubjectView, PaginatedCriteriaBuilder<SubjectView>> setting, Pageable pageable
  )
  {
    CriteriaBuilder<Subject> criteriaBuilder = criteriaBuilderFactory.create(entityManager, Subject.class);
    return subjectPageableViewRepository.findAll(setting, criteriaBuilder, pageable);
  }

  /**
   * @return {@link Set} of {@link SubjectView} matching provided {@code setting}.
   */
  public Set<SubjectView> findAll(EntityViewSetting<SubjectView, CriteriaBuilder<SubjectView>> setting)
  {
    CriteriaBuilder<Subject> criteriaBuilder = subjectViewRepository.entityCriteriaBuilder(Subject.class);
    return subjectViewRepository.findAll(setting, criteriaBuilder);
  }

  /**
   * privilege lookup ( does subject has privilege ) using the privilege as the base table of the query
   */
  public boolean hasPrivilegeViaPrivilege(Subject subject, String privilege)
  {
    CriteriaBuilder<Integer> criteria = criteriaBuilderFactory.create(entityManager, Integer.class)
      .from(Privilege.class, "privilege")
      .select("1")
      .where("privilege.name").eq(privilege);

    // We have to handle users differently since user can inherit privileges by their groups
    if (subject instanceof User) {
      // Select all groups where user is member of
      // TODO: using this as a subquery does not really work
//      var groupIds = criteriaBuilderFactory.create(entityManager, String.class)
//        .from(Group.class, "grp").select("id")
//        .where("grp.members.id").eq(subject.getId())
//        .getQuery();

      // ArrayList of long ids
      var groupIds = criteriaBuilderFactory.create(entityManager, String.class)
        .from(Group.class, "grp").select("id")
        .where("grp.members.id").eq(subject.getId())
        .getResultList();

      criteria.whereOr()
        .where("privilege.roles.globalRoleMembership.subject.id").in(groupIds)
        .where("privilege.roles.globalRoleMembership.subject.id").in(subject.getId())
      .endOr();
    }
    else {
      criteria.where("privilege.roles.globalRoleMembership.subject.id").eq(subject.getId());
    }

    return !criteria.setMaxResults(1).getResultList().isEmpty(); // Expression is negated!!!
  }

  /**
   * privilege lookup ( does subject has privilege ) using the GlobalRoleMembership as base table
   */
  public boolean hasPrivilegeViaGlobalRoleMembership(Subject subject, String privilege)
  {
    return !criteriaBuilderFactory.create(entityManager, Integer.class)
      .from(GlobalRoleMembership.class, "globalRoleMembership")
      .select("1")
      .where("globalRoleMembership.subject.id").eq(subject.getId())
      .where("globalRoleMembership.role.privileges.name").eq(privilege)
      .setMaxResults(1)
      .getResultList()
      .isEmpty(); // Expression is negated!!!
  }

  /**
   * privilege lookup ( does subject has privilege ) using the subject table as base table
   */
  public boolean hasPrivilegeViaSubject(Subject subject, String privilege)
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

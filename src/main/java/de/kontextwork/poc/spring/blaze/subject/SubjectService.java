package de.kontextwork.poc.spring.blaze.subject;

import com.blazebit.persistence.*;
import com.blazebit.persistence.view.EntityViewSetting;
import de.kontextwork.poc.spring.blaze.core.PageableEntityViewRepository;
import de.kontextwork.poc.spring.blaze.core.RegularEntityViewRepository;
import de.kontextwork.poc.spring.blaze.subject.model.domain.group.GroupSubjectView;
import de.kontextwork.poc.spring.blaze.subject.model.domain.subject.SubjectView;
import de.kontextwork.poc.spring.blaze.subject.model.domain.user.UserSubjectView;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.*;
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
  private final SubjectRepository subjectRepository;
  private final RoleMemberRepository roleMemberRepository;
  private final CriteriaBuilderFactory criteriaBuilderFactory;
  private final PageableEntityViewRepository<User> userViewRepository;
  private final PageableEntityViewRepository<Group> groupViewRepository;
  private final PageableEntityViewRepository<Subject> subjectViewRepository;
  private final RegularEntityViewRepository<Subject, Long> regularEntityViewRepository;

  public User create(final User user)
  {
    return subjectRepository.save(user);
  }

  public Group create(final Group group)
  {
    return subjectRepository.save(group);
  }

  public Role create(final Role role)
  {
    return roleRepository.save(role);
  }

  public RoleMember assign(final Role role, final Subject subject)
  {
    return roleMemberRepository.save(new RoleMember(role, subject));
  }

  public Page<UserSubjectView> getUsers(
    EntityViewSetting<UserSubjectView, PaginatedCriteriaBuilder<UserSubjectView>> setting, Pageable pageable
  )
  {
    CriteriaBuilder<User> criteriaBuilder = criteriaBuilderFactory.create(entityManager, User.class);
    return userViewRepository.findAll(setting, criteriaBuilder, pageable);
  }

  public Page<GroupSubjectView> getGroups(
    EntityViewSetting<GroupSubjectView, PaginatedCriteriaBuilder<GroupSubjectView>> setting, Pageable pageable
  )
  {
    CriteriaBuilder<Group> criteriaBuilder = criteriaBuilderFactory.create(entityManager, Group.class);
    return groupViewRepository.findAll(setting, criteriaBuilder, pageable);
  }

  public Page<SubjectView> getSubjects(
    EntityViewSetting<SubjectView, PaginatedCriteriaBuilder<SubjectView>> setting, Pageable pageable
  )
  {
    CriteriaBuilder<Subject> criteriaBuilder = criteriaBuilderFactory.create(entityManager, Subject.class);
    return subjectViewRepository.findAll(setting, criteriaBuilder, pageable);
  }

  public Set<SubjectView> getSubjects(
    EntityViewSetting<SubjectView, CriteriaBuilder<SubjectView>> setting
  )
  {
    CriteriaBuilder<Subject> criteriaBuilder = regularEntityViewRepository.entityCriteriaBuilder(Subject.class);
    return regularEntityViewRepository.findAll(setting, criteriaBuilder);
  }
}

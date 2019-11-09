package de.kontextwork.poc.spring.blaze.fullapp.subject.group;

import com.blazebit.persistence.*;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.google.common.collect.Sets;
import de.kontextwork.poc.spring.blaze.core.PageableEntityViewRepository;
import de.kontextwork.poc.spring.blaze.core.RegularEntityViewRepository;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.jpa.Group;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.view.UserIdView;
import java.util.*;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService
{
  private final EntityManager entityManager;
  private final CriteriaBuilderFactory criteriaBuilderFactory;
  private final PageableEntityViewRepository<Group> pageableEntityViewRepository;
  private final RegularEntityViewRepository<Group, Long> regularEntityViewRepository;
  private final EntityViewManager entityViewManager;

  @Transactional
  public <T extends GroupIdView> Optional<T> getOne(EntityViewSetting<T, CriteriaBuilder<T>> setting, Long id)
  {
    return regularEntityViewRepository.getOne(setting, id);
  }

  /**
   * @return {@link Page} of {@link GroupExcerptView} matching provided {@code setting}.
   */
  @Transactional
  public Page<GroupExcerptView> findAll(
    EntityViewSetting<GroupExcerptView, PaginatedCriteriaBuilder<GroupExcerptView>> setting, Pageable pageable
  )
  {
    CriteriaBuilder<Group> criteriaBuilder = criteriaBuilderFactory.create(entityManager, Group.class);
    return pageableEntityViewRepository.findAll(setting, criteriaBuilder, pageable);
  }

  @Transactional
  public GroupIdView create(final GroupCreateView groupCreateView)
  {
    final GroupCreateView createdView = regularEntityViewRepository.create(groupCreateView);
    return entityViewManager.convert(createdView, GroupIdView.class);
  }

  @Transactional
  public void limitMembersTo(GroupMemberUpdateView groupMemberUpdateView)
  {
    // Why this flush?
    entityManager.flush();
    regularEntityViewRepository.update(groupMemberUpdateView);
  }


  @SuppressWarnings("unused")
  @Transactional
  public void removeMember(Long id, UserIdView member)
  {
    HashSet<UserIdView> membersToBeRemoved = Sets.newHashSet(member);
    removeMembers(id, membersToBeRemoved);
  }


  @Transactional
  public void removeMembers(Long id, Set<UserIdView> membersToBeRemoved)
  {
    final GroupMemberUpdateView groupMemberUpdateView =
      entityViewManager.find(entityManager, GroupMemberUpdateView.class, id);

    if (groupMemberUpdateView.getMembers() != null) {
      groupMemberUpdateView.getMembers().removeAll(membersToBeRemoved);
      limitMembersTo(groupMemberUpdateView);
    }
  }
}

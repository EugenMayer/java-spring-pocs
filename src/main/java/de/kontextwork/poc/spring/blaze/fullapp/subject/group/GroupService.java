package de.kontextwork.poc.spring.blaze.fullapp.subject.group;

import com.blazebit.persistence.*;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.google.common.collect.Sets;
import de.kontextwork.poc.spring.blaze.core.PageableEntityViewRepository;
import de.kontextwork.poc.spring.blaze.core.RegularEntityViewRepository;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.jpa.Group;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.view.UserIdView;
import java.util.*;
import java.util.stream.Collectors;
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
    final GroupCreateView createdView = regularEntityViewRepository.create(groupCreateView, GroupCreateView.class);
    return entityViewManager.convert(createdView, GroupIdView.class);
  }

  public void limitMembersTo(Long id, GroupMemberUpdateView groupMemberUpdateView)
  {
    // Why this flush?
    entityManager.flush();

    // we need to create a new real update view here already and not let RegularEntityViewRepository
    // care about it, since we need to set a set of UserIdView's while a GroupMemberUpdateViewDTO does only take
    // UserIdViewDTO
    GroupMemberUpdateView groupMemberUpdateViewQualified = entityViewManager.getReference(
      GroupMemberUpdateView.class, id
    );
    // Ensure all our nested EntityViews are actual EntityViews and not any other classes just deriving from it,
    // like RoleId or SpaceId - during persistence of a Space SpaceRoleMembershipCreateView those need to be
    // proper EVs, so BP / EVM does understand which Entity they belong too.
    final Set<UserIdView> membersAsRealEntityViews = groupMemberUpdateView.getMembers().stream()
      .map(userDto -> entityViewManager.getReference(UserIdView.class, userDto.getId()))
      .collect(Collectors.toSet());
    groupMemberUpdateViewQualified.setMembers(membersAsRealEntityViews);

    regularEntityViewRepository.update(id, groupMemberUpdateViewQualified, GroupMemberUpdateView.class);
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
      limitMembersTo(id, groupMemberUpdateView);
    }
  }
}

package de.kontextwork.poc.spring.blaze.fullapp.subject.group;

import com.blazebit.persistence.*;
import com.blazebit.persistence.view.EntityViewSetting;
import de.kontextwork.poc.spring.blaze.core.PageableEntityViewRepository;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.view.GroupView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.jpa.Group;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService
{
  private final EntityManager entityManager;
  private final CriteriaBuilderFactory criteriaBuilderFactory;
  private final PageableEntityViewRepository<Group> groupPageableViewRepository;


  /**
   * @return {@link Page} of {@link GroupView} matching provided {@code setting}.
   */
  public Page<GroupView> findAll(
    EntityViewSetting<GroupView, PaginatedCriteriaBuilder<GroupView>> setting, Pageable pageable
  )
  {
    CriteriaBuilder<Group> criteriaBuilder = criteriaBuilderFactory.create(entityManager, Group.class);
    return groupPageableViewRepository.findAll(setting, criteriaBuilder, pageable);
  }
}

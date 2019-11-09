package de.kontextwork.poc.spring.blaze.fullapp.subject.user;

import com.blazebit.persistence.*;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import de.kontextwork.poc.spring.blaze.core.PageableEntityViewRepository;
import de.kontextwork.poc.spring.blaze.core.RegularEntityViewRepository;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.view.GroupIdView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.jpa.User;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.view.*;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSerivce
{
  private final EntityManager entityManager;
  private final CriteriaBuilderFactory criteriaBuilderFactory;
  private final PageableEntityViewRepository<User> userPageableViewRepository;
  private final RegularEntityViewRepository<User, Long> regularEntityViewRepository;
  private final EntityViewManager entityViewManager;


  @Transactional
  public <T extends GroupIdView> Optional<T> getOne(EntityViewSetting<T, CriteriaBuilder<T>> setting, Long id)
  {
    return regularEntityViewRepository.getOne(setting, id);
  }

  @Transactional
  public UserIdView create(final UserCreateView userCreateView)
  {
    final UserCreateView createdView = regularEntityViewRepository.create(userCreateView);
    return entityViewManager.convert(createdView, UserIdView.class);
  }

  /**
   * @return {@link Page} of {@link UserExcerptView} matching provided {@code setting}.
   */
  public Page<UserExcerptView> findAll(
    EntityViewSetting<UserExcerptView, PaginatedCriteriaBuilder<UserExcerptView>> setting, Pageable pageable
  )
  {
    CriteriaBuilder<User> criteriaBuilder = criteriaBuilderFactory.create(entityManager, User.class);
    return userPageableViewRepository.findAll(setting, criteriaBuilder, pageable);
  }
}

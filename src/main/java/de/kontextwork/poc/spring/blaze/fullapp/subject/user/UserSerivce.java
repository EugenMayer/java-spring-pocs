package de.kontextwork.poc.spring.blaze.fullapp.subject.user;

import com.blazebit.persistence.*;
import com.blazebit.persistence.view.EntityViewSetting;
import de.kontextwork.poc.spring.blaze.core.PageableEntityViewRepository;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.view.UserView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.jpa.User;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSerivce
{
  private final EntityManager entityManager;
  private final CriteriaBuilderFactory criteriaBuilderFactory;
  private final PageableEntityViewRepository<User> userPageableViewRepository;


  /**
   * @return {@link Page} of {@link UserView} matching provided {@code setting}.
   */
  public Page<UserView> findAll(
    EntityViewSetting<UserView, PaginatedCriteriaBuilder<UserView>> setting, Pageable pageable
  )
  {
    CriteriaBuilder<User> criteriaBuilder = criteriaBuilderFactory.create(entityManager, User.class);
    return userPageableViewRepository.findAll(setting, criteriaBuilder, pageable);
  }
}
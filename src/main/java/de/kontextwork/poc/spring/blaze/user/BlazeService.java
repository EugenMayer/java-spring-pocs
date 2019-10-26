package de.kontextwork.poc.spring.blaze.user;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import de.kontextwork.poc.spring.blaze.user.model.domain.*;
import de.kontextwork.poc.spring.blaze.user.model.jpa.BlazeUser;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlazeService
{
  private final BlazeUserRepository blazeUserRepository;
  private final EntityManager entityManager;
  private final EntityViewManager entityViewManager;
  private final CriteriaBuilderFactory criteriaBuilderFactory;

  private <T> CriteriaBuilder<T> getBuilder(EntityViewSetting<T, CriteriaBuilder<T>> settings)
  {
    CriteriaBuilder<BlazeUser> cb = criteriaBuilderFactory.create(entityManager, BlazeUser.class);
    return entityViewManager.applySetting(settings, cb);
  }

  @Transactional
  public BlazeUserIdView create(BlazeUserCreateView createView)
  {
    entityViewManager.save(entityManager, createView);
    return entityViewManager.convert(createView, BlazeUserIdView.class);
  }

  @Transactional
  public BlazeUser create(BlazeUser user)
  {
    return blazeUserRepository.save(user);
  }

  @Transactional
  public <T extends BlazeUserIdView> Optional<T> getOne(Integer uid, EntityViewSetting<T, CriteriaBuilder<T>> settings)
  {
    CriteriaBuilder<T> builder = getBuilder(settings);
    builder.where("uid").eq(uid);
    return builder.getResultList().stream().findFirst();
  }

  @Transactional
  public void updateUserProfile(BlazeUserProfileUpdateView profile)
  {
    entityViewManager.save(entityManager, profile);
  }
}

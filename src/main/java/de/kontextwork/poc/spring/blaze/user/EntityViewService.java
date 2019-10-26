package de.kontextwork.poc.spring.blaze.user;

import com.blazebit.persistence.view.EntityViewManager;
import de.kontextwork.poc.spring.blaze.subject.model.domain.UserCreateView;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntityViewService
{
  private final EntityManager entityManager;
  private final EntityViewManager entityViewManager;

  @Transactional
  public void createUser(final UserCreateView userCreateView)
  {
    entityViewManager.save(entityManager, userCreateView);
  }
}

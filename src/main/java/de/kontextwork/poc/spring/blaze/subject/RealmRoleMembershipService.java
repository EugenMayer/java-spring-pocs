package de.kontextwork.poc.spring.blaze.subject;

import com.blazebit.persistence.view.EntityViewManager;
import de.kontextwork.poc.spring.blaze.core.EntityViewDtoConverter;
import de.kontextwork.poc.spring.blaze.subject.model.domain.RealmRoleMembershipCreateView;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.member.RealmRoleMembership;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.role.RealmRole;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.subject.Realm;
import de.kontextwork.poc.spring.blaze.subject.model.jpa.subject.Subject;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RealmRoleMembershipService
{
  private final RealmRoleMembershipRepository realmRoleMembershipRepository;
  private final EntityViewDtoConverter entityViewDtoConverter;
  private final EntityViewManager entityViewManager;
  private final EntityManager entityManager;

  @Transactional
  public void create(RealmRoleMembershipCreateView realmRoleMembershipCreateView)
  {
    final RealmRoleMembershipCreateView qualifiedCreateView = entityViewDtoConverter.qualifyEntityCreateView(
      realmRoleMembershipCreateView,
      RealmRoleMembershipCreateView.class
    );

    entityViewManager.save(entityManager, qualifiedCreateView);
  }

  /**
   * Creates new {@link RealmRoleMembership} from provided {@code realm}, {@code role} and {@code subject}.
   */
  public void assign(final Realm realm, final RealmRole role, final Subject subject)
  {
    realmRoleMembershipRepository.save(new RealmRoleMembership(realm, role, subject));
  }
}

package de.kontextwork.poc.spring.blaze.fullapp.rolemembership;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import de.kontextwork.poc.spring.blaze.core.EntityViewDtoConverter;
import de.kontextwork.poc.spring.blaze.core.RegularEntityViewRepository;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.RealmRoleMembership;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.RealmRole;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.jpa.Realm;
import de.kontextwork.poc.spring.blaze.fullapp.subject.SubjectRepository;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.jpa.Subject;
import java.util.List;
import java.util.Set;
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
  private final CriteriaBuilderFactory criteriaBuilderFactory;
  private final RegularEntityViewRepository<RealmRoleMembership, Long> regularEntityViewRepository;

  @Transactional
  public void create(RealmRoleMembershipCreateView realmRoleMembershipCreateView)
  {
    final RealmRoleMembershipCreateView qualifiedCreateView = entityViewDtoConverter.qualifyEntityCreateView(
      realmRoleMembershipCreateView,
      RealmRoleMembershipCreateView.class
    );

    entityViewManager.save(entityManager, qualifiedCreateView);
  }

  @Transactional
  public void createUntouched(RealmRoleMembershipCreateView realmRoleMembershipCreateView)
  {
    entityViewManager.save(entityManager, realmRoleMembershipCreateView);
  }

  /**
   * Creates new {@link RealmRoleMembership} from provided {@code realm}, {@code role} and {@code subject}.
   */
  public void assign(final Realm realm, final RealmRole role, final Subject subject)
  {
    realmRoleMembershipRepository.save(new RealmRoleMembership(realm, role, subject));
  }

  public Set<RealmRoleMembershipIdView> findAll() {
    return regularEntityViewRepository.findAll(
      EntityViewSetting.create(RealmRoleMembershipIdView.class),
      RealmRoleMembership.class);
  }
}

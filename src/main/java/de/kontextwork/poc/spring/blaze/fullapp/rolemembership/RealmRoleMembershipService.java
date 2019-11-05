package de.kontextwork.poc.spring.blaze.fullapp.rolemembership;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import de.kontextwork.poc.spring.blaze.core.EntityViewDtoConverter;
import de.kontextwork.poc.spring.blaze.core.RegularEntityViewRepository;
import de.kontextwork.poc.spring.blaze.fullapp.realm.RealmService;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.jpa.Realm_;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.view.RealmIdView;
import de.kontextwork.poc.spring.blaze.fullapp.role.RoleService;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.view.RoleIdView;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.RealmRoleMembership_;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.RealmRoleMembership;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.RealmRole;
import de.kontextwork.poc.spring.blaze.fullapp.realm.model.jpa.Realm;
import de.kontextwork.poc.spring.blaze.fullapp.subject.SubjectService;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.jpa.Subject;
import de.kontextwork.poc.spring.blaze.fullapp.subject.model.view.SubjectIdView;
import java.util.Set;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RealmRoleMembershipService
{
  private final EntityViewDtoConverter entityViewDtoConverter;
  private final EntityViewManager entityViewManager;
  private final EntityManager entityManager;
  private final RegularEntityViewRepository<RealmRoleMembership, Long> regularEntityViewRepository;

  // Those are only needed because we did not migrate
  // assign(final Realm realm, final RealmRole role, final Subject subject)
  private final RoleService roleService;
  private final RealmService realmService;
  private final SubjectService subjectService;

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
   * @deprecated use {@link RealmRoleMembershipService#assign} using IdViews directly
   * Creates new {@link RealmRoleMembership} from provided {@code realm}, {@code role} and {@code subject}.
   */
  @Transactional
  @Deprecated
  public void assign(final Realm realm, final RealmRole role, final Subject subject)
  {
    this.assign(
      realmService.getOneAsIdView(realm.getId()).orElseThrow(),
      roleService.getOneAsIdView(role.getId()).orElseThrow(),
      subjectService.getOneAsIdView(subject.getId()).orElseThrow()
    );
  }

  /**
   * Creates new {@link RealmRoleMembership} from provided {@code realm}, {@code role} and {@code subject}.
   */
  @Transactional
  public void assign(final RealmIdView realm, final RoleIdView role, final SubjectIdView subject)
  {
    final RealmRoleMembershipCreateView realmRoleMembershipCreateView = entityViewManager.create(
      RealmRoleMembershipCreateView.class);

    realmRoleMembershipCreateView.setRealm(realm);
    realmRoleMembershipCreateView.setRole(role);
    realmRoleMembershipCreateView.setSubject(subject);
    this.create(realmRoleMembershipCreateView);
  }

  @Transactional
  public <V> Set<V> findAll(EntityViewSetting<V, CriteriaBuilder<V>> setting) {
    return regularEntityViewRepository.findAll(
      setting,
      RealmRoleMembership.class);
  }

  @Transactional
  public <V> Set<V> findAllByRealm(Long realmId, EntityViewSetting<V, CriteriaBuilder<V>> setting) {
    CriteriaBuilder<RealmRoleMembership> entityCriteriaBuilder = regularEntityViewRepository
      .entityCriteriaBuilder(RealmRoleMembership.class);
    entityCriteriaBuilder.where(RealmRoleMembership_.REALM + "." + Realm_.ID).eq(realmId);

    return regularEntityViewRepository.findAll(
      setting,
      entityCriteriaBuilder);
  }
}

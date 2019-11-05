package de.kontextwork.poc.spring.blaze.fullapp.rolemembership;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import de.kontextwork.poc.spring.blaze.core.EntityViewDtoConverter;
import de.kontextwork.poc.spring.blaze.core.RegularEntityViewRepository;
import de.kontextwork.poc.spring.blaze.fullapp.role.RoleService;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.view.RoleIdView;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.GlobalRoleMembership;
import de.kontextwork.poc.spring.blaze.fullapp.role.model.jpa.Role;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.jpa.RealmRoleMembership;
import de.kontextwork.poc.spring.blaze.fullapp.rolemembership.model.view.*;
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
public class GlobalRoleMembershipService
{
  private final EntityViewDtoConverter entityViewDtoConverter;
  private final EntityViewManager entityViewManager;
  private final EntityManager entityManager;
  private final RegularEntityViewRepository<GlobalRoleMembership, Long> regularEntityViewRepository;

  // Those are only needed because we did not migrate
  // assign(final Realm realm, final RealmRole role, final Subject subject)
  private final RoleService roleService;
  private final SubjectService subjectService;


  @Transactional
  public void create(GlobalRoleMembershipCreateView realmRoleMembershipCreateView)
  {
    final GlobalRoleMembershipCreateView qualifiedCreateView = entityViewDtoConverter.qualifyEntityCreateView(
      realmRoleMembershipCreateView,
      GlobalRoleMembershipCreateView.class
    );

    entityViewManager.save(entityManager, qualifiedCreateView);
  }

  /**
   * Creates new {@link GlobalRoleMembership} from provided {@code role} and {@code subject}.
   */
  public void assign(final Role role, final Subject subject)
  {
    this.assign(
      roleService.getOneAsIdView(role.getId()).orElseThrow(),
      subjectService.getOneAsIdView(subject.getId()).orElseThrow()
    );
  }

  /**
   * Creates new {@link RealmRoleMembership} from provided {@code realm}, {@code role} and {@code subject}.
   */
  @Transactional
  public void assign(final RoleIdView role, final SubjectIdView subject)
  {
    final GlobalRoleMembershipCreateView globalRoleMembershipCreateView = entityViewManager.create(
      GlobalRoleMembershipCreateView.class);

    globalRoleMembershipCreateView.setRole(role);
    globalRoleMembershipCreateView.setSubject(subject);
    this.create(globalRoleMembershipCreateView);
  }

  @Transactional
  public <V> Set<V> findAll(EntityViewSetting<V, CriteriaBuilder<V>> setting) {
    return regularEntityViewRepository.findAll(
      setting,
      GlobalRoleMembership.class);
  }
}

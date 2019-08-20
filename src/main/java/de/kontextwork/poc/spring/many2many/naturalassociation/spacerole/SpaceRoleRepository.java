package de.kontextwork.poc.spring.many2many.naturalassociation.spacerole;

import de.kontextwork.poc.spring.many2many.naturalassociation.SpaceRoleMembership;
import de.kontextwork.poc.spring.many2many.naturalassociation.space.Space;
import de.kontextwork.poc.spring.many2many.naturalassociation.user.User;
import javax.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceRoleRepository extends JpaRepository<SpaceRole, String>,
  JpaSpecificationExecutor<SpaceRole>
{
  class Specifications
  {
    public static Specification<SpaceRole> branchForSpace(Space space)
    {
      return (root, query, cb) -> {
        Join<SpaceRole, SpaceRoleMembership> legacyMappingJoin = root.join("spaceRoleMemberships");
        return cb.equal(legacyMappingJoin.get("space"), space);
      };
    }

    public static Specification<SpaceRole> branchForUserAccount(User user)
    {
      return (root, query, cb) -> {
        Join<SpaceRole, SpaceRoleMembership> legacyMappingJoin = root.join("spaceRoleMemberships");
        return cb.equal(legacyMappingJoin.get("user"), user);
      };
    }
  }
}

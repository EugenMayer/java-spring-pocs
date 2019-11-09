package de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.view;

import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.jpa.Group;
import java.util.UUID;

@EntityView(Group.class)
@CreatableEntityView
@SuppressWarnings({"unused", "common-java:DuplicatedBlocks"})
public interface GroupCreateView extends GroupIdView
{
  /**
   * is called when the factor creates the createView ( not after persistence )
   * Init our object defaults
   */
  @PostCreate
  default void initDefaults()
  {
    setGroupMachine(UUID.randomUUID().toString().substring(0, 32));
  }

  String getName();

  void setName(String name);

  String getGroupMachine();

  void setGroupMachine(String machine);
}

package de.kontextwork.poc.spring.blaze.fullapp.subject.group.controller;

import com.blazebit.persistence.view.EntityViewSetting;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.GroupService;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.view.GroupEntireView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.view.GroupMemberUpdateView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/entity/group")
public class GroupController
{
  private final GroupService groupService;

  @GetMapping("/{id}")
  public ResponseEntity<GroupEntireView> getGroup(@PathVariable Long id)
  {
    return ResponseEntity.ok(
      groupService.getOne(EntityViewSetting.create(GroupEntireView.class), id)
        .orElseThrow()
    );
  }

  /**
   * be aware, this is not additive, this will set the members to this list, removing all current members which are no
   * longer in your list and so on
   */
  @PutMapping("/{id}/member")
  public ResponseEntity<GroupEntireView> limitMembersTo(
    @PathVariable Long id,
    @RequestBody GroupMemberUpdateView groupMemberUpdateView
  )
  {
    // just ensure
    groupService.limitMembersTo(groupMemberUpdateView);
    return ResponseEntity.ok(
      groupService.getOne(EntityViewSetting.create(GroupEntireView.class), id).orElseThrow()
    );
  }
}

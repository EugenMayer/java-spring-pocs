package de.kontextwork.poc.spring.blaze.fullapp.subject.group.controller;

import com.blazebit.persistence.view.EntityViewManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.api.TestDTO;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.view.GroupIdView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.group.model.view.GroupMemberUpdateView;
import de.kontextwork.poc.spring.blaze.fullapp.subject.user.model.view.UserIdView;
import de.kontextwork.poc.spring.blaze.fullapp.testUtils.scenario.GroupScenarioCreator;
import de.kontextwork.poc.spring.blaze.fullapp.testUtils.scenario.UserScenarioCreator;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@DataJpaTest(properties = {"blazepersistance.enabled=true"})
//@AutoConfigureWebMvc
//@AutoConfigureMockMvc
//@Import({
//  UserSerivce.class,
//  GroupService.class,
//  JpaBlazeConfiguration.class,
//  BlazePersistenceConfiguration.class,
//  PageableEntityViewRepository.class,
//  RegularEntityViewRepository.class,
//  EntityViewDtoConverter.class,
//  ModelMapper.class,
//  GroupScenarioCreator.class,
//  UserScenarioCreator.class,
//})
//@ImportAutoConfiguration
@SpringBootTest(properties = {
  "blazepersistance.enabled=true",
  "logging.level.org.springframework.web=TRACE"
})
@AutoConfigureMockMvc
@Import({
  ModelMapper.class
})
class GroupControllerTest
{
  @Autowired
  EntityViewManager entityViewManager;
  @Autowired
  GroupScenarioCreator groupScenarioCreator;
  @Autowired
  UserScenarioCreator userScenarioCreator;

//  @MockBean
//  private GroupService groupService;

//  @Test
//  void getGroupMocked() throws Exception
//  {
//    Long groupId = 123L;
//    GroupEntireView groupEntireView = entityViewManager.getReference(GroupEntireView.class, groupId);
//
//    doReturn(groupEntireView).when(groupService).getOne(eq(groupId));
//    mockMvc
//      .perform(
//        get(String.format("entity/group/%d", groupId))
//      )
//      .andDo(print())
//      .andExpect(status().isOk());
//  }
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void getGroup() throws Exception
  {
    final GroupIdView groupIdView = groupScenarioCreator.createGroup("test", "testMachine");
    mockMvc
      .perform(
        get(String.format("/entity/group/%d", groupIdView.getId()))
      )
      .andDo(print())
      .andExpect(status().isOk());
  }

  @Test
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void limitMembersTo() throws Exception
  {
    final GroupIdView groupIdView = groupScenarioCreator.createGroup("test", "testMachine");
    final UserIdView userRed = userScenarioCreator.createUser("Max", "Mustermann");
    final UserIdView userBlue = userScenarioCreator.createUser("Dax", "Daxmann");

    // our update view we are going to use a the payload
    GroupMemberUpdateView groupMemberUpdateView = entityViewManager.getReference(
      GroupMemberUpdateView.class,
      groupIdView.getId()
    );
    groupMemberUpdateView.setMembers(Sets.newHashSet(userRed, userBlue));

    mockMvc
      .perform(
        put(String.format("/entity/group/%d/member", groupIdView.getId()))
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(groupMemberUpdateView))
      )
      .andDo(print())
      .andExpect(status().isOk());
    // we might add an assert on the content
  }

  @Test
  @Sql(
    statements = "alter table subject_user modify uid bigint auto_increment;",
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
  )
  void testDto() throws Exception
  {
    List<TestDTO> payload = new ArrayList<>();
    payload.add(TestDTO.builder().name("test1").build());
    payload.add(TestDTO.builder().name("test2").build());

    mockMvc
      .perform(
        post("/entity/group/testdto")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(payload))
      )
      .andDo(print())
      .andExpect(status().isOk());
    // we might add an assert on the content
  }
}
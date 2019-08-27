package de.kontextwork.poc.spring.many2many.pk.web;

import com.google.common.collect.Sets;
import de.kontextwork.poc.spring.many2many.pk.domain.ChildPkBased;
import de.kontextwork.poc.spring.many2many.pk.domain.ParentPkBased;
import de.kontextwork.poc.spring.many2many.pk.repository.ParentPkBasedRepository;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
  "spring.jpa.hibernate.ddl-auto=create-drop",
  // this is the main point for this test, disabling the Transaction in the view should
  // break the lazy mode here
  "spring.jpa.open-in-view=false"
})
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class ParentPkBasedControllerTest
{
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  EntityManager entityManager;

  @Autowired
  ParentPkBasedRepository parentPkBasedRepository;

  @Test
  @DisplayName("Ensure we can retrieve the projection including lazy load in a rest controller while spring.jpa.open-in-view=false")
  void getAllParents() throws Exception
  {
    var parent1 = new ParentPkBased();
    parent1.setChildren(
      Sets.newHashSet(new ChildPkBased("child1"), new ChildPkBased("child2"))
    );
    parentPkBasedRepository.saveAndFlush(parent1);

    var parent2 = new ParentPkBased();
    parent2.setChildren(
      Sets.newHashSet(new ChildPkBased("child3"))
    );
    parentPkBasedRepository.saveAndFlush(parent2);

    // Ensure our L1 cache is empty before we triggerthe controller
    // to load the entities, so we ensure lazy is used and nothing is populated
    //entityManager.flush();
    //entityManager.clear();

    mockMvc
      .perform(
        get("/pk/parent")
      )
      .andDo(print())
      .andExpect(status().isOk());
  }
}
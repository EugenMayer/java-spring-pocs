package de.kontextwork.poc.spring.blaze.pageable;

import com.blazebit.persistence.*;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import de.kontextwork.poc.spring.blaze.pageable.model.domain.CatExcerptView;
import de.kontextwork.poc.spring.blaze.pageable.model.jpa.Cat;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CatService
{
  private final EntityManager entityManager;
  private final EntityViewManager entityViewManager;
  private final CriteriaBuilderFactory criteriaBuilderFactory;

  private final CatRepository catRepository;

  @Transactional
  public Cat create(Cat cat)
  {
    return catRepository.save(cat);
  }

  @Transactional
  public List<Cat> create(List<Cat> cats)
  {
    return catRepository.saveAll(cats);
  }

  @Transactional
  public Page<Cat> read(Pageable pageable)
  {
    return catRepository.findAll(pageable);
  }

  @Transactional
  public PagedList<CatExcerptView> readExcerpt(Pageable pageable)
  {
    int firstResult = pageable.getPageNumber() * pageable.getPageSize();
    int maxResults = pageable.getPageSize();

    EntityViewSetting<CatExcerptView, PaginatedCriteriaBuilder<CatExcerptView>> setting =
      EntityViewSetting.create(CatExcerptView.class, firstResult, maxResults);

    CriteriaBuilder<CatExcerptView> catExcerptViewCriteriaBuilder = criteriaBuilderFactory
      .create(entityManager, CatExcerptView.class)
      .setMaxResults(maxResults);

    return entityViewManager.applySetting(setting, catExcerptViewCriteriaBuilder)
      .getResultList();
  }
}

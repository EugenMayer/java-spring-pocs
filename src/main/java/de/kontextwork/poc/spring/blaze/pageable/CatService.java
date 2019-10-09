package de.kontextwork.poc.spring.blaze.pageable;

import com.blazebit.persistence.*;
import com.blazebit.persistence.view.*;
import de.kontextwork.poc.spring.blaze.pageable.model.domain.CatExcerptView;
import de.kontextwork.poc.spring.blaze.pageable.model.jpa.Cat;
import de.kontextwork.poc.spring.blaze.pageable.model.page.EntityViewPage;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
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
  public Page<CatExcerptView> readExcerpt(Pageable pageable)
  {
    int firstResult = pageable.getPageNumber() * pageable.getPageSize();
    int maxResults = pageable.getPageSize();

    EntityViewSetting<CatExcerptView, PaginatedCriteriaBuilder<CatExcerptView>> setting =
      EntityViewSetting.create(CatExcerptView.class, firstResult, maxResults);

    CriteriaBuilder<Cat> catExcerptViewCriteriaBuilder = criteriaBuilderFactory
      .create(entityManager, Cat.class);

    applySorting(pageable, catExcerptViewCriteriaBuilder);

    final PagedList<CatExcerptView> resultList = entityViewManager.applySetting(setting, catExcerptViewCriteriaBuilder)
      .getResultList();
    
    return EntityViewPage.of(resultList, pageable);
  }

  private void applySorting(Pageable source, CriteriaBuilder<?> target)
  {
    for (Order sort : source.getSort()) {
      target.orderBy(sort.getProperty(), Direction.ASC == sort.getDirection(), false);
    }
  }
}

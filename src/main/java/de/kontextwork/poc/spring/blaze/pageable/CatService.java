package de.kontextwork.poc.spring.blaze.pageable;

import com.blazebit.persistence.*;
import com.blazebit.persistence.view.EntityViewSetting;
import de.kontextwork.poc.spring.blaze.core.EntityViewSettingFactory;
import de.kontextwork.poc.spring.blaze.core.PageableEntityViewRepository;
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
  private final CriteriaBuilderFactory criteriaBuilderFactory;
  private final CatRepository catRepository;
  private final PageableEntityViewRepository<Cat> pageableEntityViewRepository;

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
  public Page<CatExcerptView> getAll(Pageable pageable)
  {
    var setting = EntityViewSettingFactory.create(CatExcerptView.class, pageable);
    return pageableEntityViewRepository.findAll(Cat.class, CatExcerptView.class, setting, pageable);
  }

  @Transactional
  public Page<CatExcerptView> getAll(
    EntityViewSetting<CatExcerptView, PaginatedCriteriaBuilder<CatExcerptView>> setting,
    Pageable pageable
  )
  {
    return pageableEntityViewRepository.findAll(Cat.class, CatExcerptView.class, setting, pageable);
  }

  @Transactional
  public Page<CatExcerptView> getAllBlackCats(
    EntityViewSetting<CatExcerptView, PaginatedCriteriaBuilder<CatExcerptView>> setting,
    Pageable pageable
  )
  {
    final CriteriaBuilder<Cat> catCriteriaBuilder = criteriaBuilderFactory.create(entityManager, Cat.class);
    catCriteriaBuilder.where("color").eq("black");

    return pageableEntityViewRepository.findAll(Cat.class, CatExcerptView.class, setting, catCriteriaBuilder, pageable);
  }
}

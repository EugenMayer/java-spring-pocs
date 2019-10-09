package de.kontextwork.poc.spring.blaze.pageable;

import de.kontextwork.poc.spring.blaze.pageable.model.jpa.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

interface CatRepository extends PagingAndSortingRepository<Cat, Long>, JpaRepository<Cat, Long>
{
}

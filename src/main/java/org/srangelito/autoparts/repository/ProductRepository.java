package org.srangelito.autoparts.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.srangelito.autoparts.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, String> { ;
    Page<ProductEntity> findAllByPartNumberContainingIgnoreCase(String partNumber, Pageable pageable);
    Page<ProductEntity> findAllByApplicationContainingIgnoreCase(String application, Pageable pageable);
}

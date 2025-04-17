package org.srangelito.autoparts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.srangelito.autoparts.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {
}

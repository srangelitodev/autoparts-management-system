package org.srangelito.autoparts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.srangelito.autoparts.entity.SaleEntity;

@Repository
public interface SaleRepository extends JpaRepository<SaleEntity, Integer> {
}

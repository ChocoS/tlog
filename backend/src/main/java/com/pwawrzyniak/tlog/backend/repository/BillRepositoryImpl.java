package com.pwawrzyniak.tlog.backend.repository;

import com.pwawrzyniak.tlog.backend.entity.Bill;
import com.pwawrzyniak.tlog.backend.entity.BillItem;
import com.pwawrzyniak.tlog.backend.entity.BillItem_;
import com.pwawrzyniak.tlog.backend.entity.Bill_;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;

@Repository
public class BillRepositoryImpl implements BillRepositoryCustom {

  @PersistenceContext
  private EntityManager entityManager;

  public BigDecimal totalCostOfAllNotDeletedBySearchString(String searchString) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

    CriteriaQuery<BigDecimal> billCriteriaQuery = criteriaBuilder.createQuery(BigDecimal.class);
    Root<Bill> billRoot = billCriteriaQuery.from(Bill.class);
    Join<Bill, BillItem> billItemJoin = billRoot.join(Bill_.billItems);
    billCriteriaQuery.select(criteriaBuilder.sum(billItemJoin.get(BillItem_.cost)));
    billCriteriaQuery.where(BillSpecifications.notDeletedAndFreeTextSearch(searchString)
        .toPredicate(billRoot, billCriteriaQuery, criteriaBuilder));
    return entityManager.createQuery(billCriteriaQuery).getSingleResult();
  }
}
package com.pwawrzyniak.tlog.backend.repository;

import com.pwawrzyniak.tlog.backend.entity.Bill;
import com.pwawrzyniak.tlog.backend.entity.BillItem;
import com.pwawrzyniak.tlog.backend.entity.BillItem_;
import com.pwawrzyniak.tlog.backend.entity.Bill_;
import com.pwawrzyniak.tlog.backend.entity.Tag;
import com.pwawrzyniak.tlog.backend.entity.Tag_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.persistence.criteria.Subquery;
import java.math.BigDecimal;

public class BillSpecifications {

  public static Specification<Bill> notDeleted() {
    return (Root<Bill> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
        builder.equal(root.get(Bill_.deleted), Boolean.FALSE);
  }

  public static Specification<Bill> freeTextSearch(String searchString) {
    return dateLike(searchString)
        .or(tagLike(searchString))
        .or(descriptionLike((searchString)))
        .or(costLike(searchString))
        .or(totalCostLike(searchString));
  }

  private static Specification<Bill> dateLike(String searchString) {
    return (Root<Bill> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
      if (searchString != null && searchString.length() > 0) {
        return builder.like(
            builder.function("TO_CHAR", String.class, root.get(Bill_.date), builder.literal("yyyy-MM-dd")),
            withWildcards(searchString)
        );
      } else {
        return builder.and(); // true
      }
    };
  }

  private static Specification<Bill> tagLike(String searchString) {
    return (Root<Bill> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
      if (searchString != null && searchString.length() > 0) {
        query.distinct(true);
        ListJoin<Bill, BillItem> billItemJoin = root.join(Bill_.billItems);
        SetJoin<BillItem, Tag> tagJoin = billItemJoin.join(BillItem_.tags);
        return builder.like(tagJoin.get(Tag_.name), withWildcards(searchString));
      } else {
        return builder.and(); // true
      }
    };
  }

  private static Specification<Bill> descriptionLike(String searchString) {
    return (Root<Bill> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
      if (searchString != null && searchString.length() > 0) {
        query.distinct(true);
        ListJoin<Bill, BillItem> billItemJoin = root.join(Bill_.billItems);
        return builder.like(
            builder.function("LOWER", String.class, billItemJoin.get(BillItem_.description)),
            withWildcards(searchString.toLowerCase()));
      } else {
        return builder.and(); // true
      }
    };
  }

  private static Specification<Bill> costLike(String searchString) {
    return (Root<Bill> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
      if (searchString != null && searchString.length() > 0) {
        query.distinct(true);
        ListJoin<Bill, BillItem> billItemJoin = root.join(Bill_.billItems);
        return builder.like(
            builder.function("TO_CHAR", String.class, billItemJoin.get(BillItem_.cost)),
            withWildcards(searchString)
        );
      } else {
        return builder.and(); // true
      }
    };
  }

  private static Specification<Bill> totalCostLike(String searchString) {
    return (Root<Bill> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
      if (searchString != null && searchString.length() > 0) {
        Subquery<BigDecimal> sub = query.subquery(BigDecimal.class);
        Root subRoot = sub.from(BillItem.class);
        sub.select(builder.sum(subRoot.get(BillItem_.cost)));
        sub.where(builder.equal(root.get(Bill_.id), subRoot.get(BillItem_.bill).get(Bill_.id)));
        return builder.like(
            builder.function("TO_CHAR", String.class, sub),
            withWildcards(searchString)
        );
      } else {
        return builder.and(); // true
      }
    };
  }

  private static String withWildcards(String searchString) {
    return "%" + searchString + "%";
  }
}
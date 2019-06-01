package com.pwawrzyniak.tlog.backend.repository;

import java.math.BigDecimal;

public interface BillRepositoryCustom {

  BigDecimal totalCostOfAllNotDeletedBySearchString(String searchString);
}
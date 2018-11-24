package com.pwawrzyniak.tlog.backend.service;

import com.pwawrzyniak.tlog.backend.dto.BillDto;
import com.pwawrzyniak.tlog.backend.entity.Bill;
import com.pwawrzyniak.tlog.backend.repository.BillRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillService {

  private static Logger log = LoggerFactory.getLogger(BillService.class);

  @Autowired
  private BillRepository billRepository;

  @Autowired
  private EntityToDtoConverter entityToDtoConverter;

  @Autowired
  private DtoToEntityConverter dtoToEntityConverter;

  @Transactional
  public List<BillDto> findAllBills() {
    List<BillDto> billDtoList = billRepository.findAll().stream().map(entityToDtoConverter::convertBill).collect(Collectors.toList());
    log.info("Found {} bills", billDtoList.size());
    return billDtoList;
  }

  @Transactional
  public void saveBill(BillDto billDto) {
    Bill bill = billRepository.save(dtoToEntityConverter.convertBillDto(billDto));
    log.info("New bill saved with id {}: {}", bill.getId(), billDto);
  }
}
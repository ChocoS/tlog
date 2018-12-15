package com.pwawrzyniak.tlog.backend.service;

import com.pwawrzyniak.tlog.backend.dto.BillDto;
import com.pwawrzyniak.tlog.backend.entity.Bill;
import com.pwawrzyniak.tlog.backend.entity.BillItem;
import com.pwawrzyniak.tlog.backend.repository.BillRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BillService {

  private static Logger log = LoggerFactory.getLogger(BillService.class);

  @Autowired
  private Validator validator;

  @Autowired
  private BillRepository billRepository;

  @Autowired
  private EntityToDtoConverter entityToDtoConverter;

  @Autowired
  private DtoToEntityConverter dtoToEntityConverter;

  @Transactional
  public List<BillDto> findAllNotDeletedBills() {
    List<BillDto> billDtoList = billRepository.findByDeletedOrderByDateDesc(false).stream().map(entityToDtoConverter::convertBill).collect(Collectors.toList());
    log.info("Found {} bills", billDtoList.size());
    return billDtoList;
  }

  @Transactional
  public boolean saveBill(BillDto billDto) {
    if (isValid(billDto)) {
      if (billDto.getId() != null) {
        Optional<Bill> optionalBillToBeUpdated = billRepository.findById(billDto.getId());
        if (optionalBillToBeUpdated.isPresent()) {
          Bill billToBeUpdated = optionalBillToBeUpdated.get();
          updateBill(billToBeUpdated, dtoToEntityConverter.convertBillDto(billDto));
          log.info("Bill was updated: {}", billDto);
          return true;
        }
      } else {
        Bill bill = billRepository.save(dtoToEntityConverter.convertBillDto(billDto));
        log.info("New bill saved with id {}: {}", bill.getId(), billDto);
        return true;
      }
    }
    return false;
  }

  private void updateBill(Bill billToBeUpdated, Bill editedBillFromForm) {
    billToBeUpdated.setDate(editedBillFromForm.getDate());

    int current = 0;
    int billItemsToBeUpdatedSize = billToBeUpdated.getBillItems().size();
    for (BillItem editedBillItem : editedBillFromForm.getBillItems()) {
      if (current < billItemsToBeUpdatedSize) {
        BillItem billItemToBeUpdated = billToBeUpdated.getBillItems().get(current);
        updateBillItem(editedBillItem, billItemToBeUpdated);
      } else {
        editedBillItem.setBill(billToBeUpdated);
        billToBeUpdated.getBillItems().add(editedBillItem);
      }
      current++;
    }
    if (editedBillFromForm.getBillItems().size() < billToBeUpdated.getBillItems().size()) {
      billToBeUpdated.getBillItems().subList(editedBillFromForm.getBillItems().size(), billToBeUpdated.getBillItems().size()).clear();
    }
  }

  private void updateBillItem(BillItem editedBillItem, BillItem billItemToBeUpdated) {
    if (!billItemToBeUpdated.getCost().equals(editedBillItem.getCost())) {
      billItemToBeUpdated.setCost(editedBillItem.getCost());
    }
    if (!billItemToBeUpdated.getDescription().equals(editedBillItem.getDescription())) {
      billItemToBeUpdated.setDescription(editedBillItem.getDescription());
    }
    if (!billItemToBeUpdated.getExpression().equals(editedBillItem.getExpression())) {
      billItemToBeUpdated.setExpression(editedBillItem.getExpression());
    }
    if (!billItemToBeUpdated.getTags().equals(editedBillItem.getTags())) {
      billItemToBeUpdated.setTags(editedBillItem.getTags());
    }
  }

  @Transactional
  public boolean softDeleteBill(BillDto billDto) {
    if (billDto != null && billDto.getId() != null) {
      Optional<Bill> optionalBill = billRepository.findById(billDto.getId());
      if (optionalBill.isPresent()) {
        Bill bill = optionalBill.get();
        bill.setDeleted(true);
        return true;
      }
    }
    return false;
  }

  public Set<ConstraintViolation<BillDto>> validate(BillDto billDto) {
    return validator.validate(billDto);
  }

  private boolean isValid(BillDto billDto) {
    return validate(billDto).isEmpty();
  }
}
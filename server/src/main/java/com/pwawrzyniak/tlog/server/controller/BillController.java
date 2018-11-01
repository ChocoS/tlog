package com.pwawrzyniak.tlog.server.controller;

import com.pwawrzyniak.tlog.backend.model.Bill;
import com.pwawrzyniak.tlog.backend.service.MockDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.pwawrzyniak.tlog.server.controller.ControllerPaths.BILL;

@RestController
public class BillController {

  @Autowired
  MockDataProvider mockDataProvider;

  @GetMapping(BILL)
  public List<Bill> getBill() {

    return mockDataProvider.getMockData();
  }
}
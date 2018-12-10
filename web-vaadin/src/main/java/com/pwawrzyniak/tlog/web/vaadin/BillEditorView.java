package com.pwawrzyniak.tlog.web.vaadin;

import com.pwawrzyniak.tlog.backend.dto.BillDto;
import com.pwawrzyniak.tlog.backend.service.BillService;
import com.pwawrzyniak.tlog.backend.service.TagService;
import com.pwawrzyniak.tlog.web.vaadin.events.Broadcaster;
import com.pwawrzyniak.tlog.web.vaadin.events.Event;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.pwawrzyniak.tlog.web.vaadin.events.Event.Type.SAVE_BILL;
import static com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER;

@SpringComponent
@UIScope
public class BillEditorView extends VerticalLayout {

  private static Logger log = LoggerFactory.getLogger(BillEditorView.class);

  private static final int DEFAULT_NUMBER_OF_BILL_ITEMS = 3;
  private static final int MAX_NUMBER_OF_BILL_ITEMS = 10;

  private List<String> tags;
  private List<BillItemEditorView> billItemEditorViews = new ArrayList<>();
  private TextField totalValueTextField = new TextField("Total");

  private TagService tagService;

  public BillEditorView(@Autowired BillService billService, @Autowired TagService tagService) {
    this.tagService = tagService;
    tags = tagService.findAllTagsSorted();

    IntStream.range(0, DEFAULT_NUMBER_OF_BILL_ITEMS).forEach(i -> billItemEditorViews.add(new BillItemEditorView(tags, this)));

    DatePicker billDate = new DatePicker("Date");
    billDate.setMax(LocalDate.now());

    totalValueTextField.setValue("0");
    totalValueTextField.setReadOnly(true);
    totalValueTextField.setTabIndex(-1);

    Button addBillItemButton = new Button("Add bill item", event -> {
      if (billItemEditorViews.size() < MAX_NUMBER_OF_BILL_ITEMS) {
        BillItemEditorView billItemEditorView = new BillItemEditorView(tags, this);
        billItemEditorViews.add(billItemEditorView);
        this.add(billItemEditorView);
      }
    });
    Button removeBillItemButton = new Button("Remove bill item", event -> {
      if (billItemEditorViews.size() > 1) {
        BillItemEditorView billItemEditorView = billItemEditorViews.get(billItemEditorViews.size() - 1);
        billItemEditorViews.remove(billItemEditorView);
        this.remove(billItemEditorView);
        recalculateTotal();
      }
    });
    Button saveBillButton = new Button("Save bill", event -> {
      BillDto billDto = BillDto.builder().date(billDate.getValue())
          .billItems(billItemEditorViews.stream().map(BillItemEditorView::readBillItemDto)
              .filter(Objects::nonNull).collect(Collectors.toList()))
          .build();
      Set<ConstraintViolation<BillDto>> constraintViolations = billService.validate(billDto);
      if (constraintViolations.isEmpty()) {
        billService.saveBill(billDto);
        clearBillForms();
        showConfirmation(billDto);
        fireSaveNewBillEvent();
      } else {
        List<String> messageLines = new ArrayList<>();
        messageLines.add("Bill is not valid! Violations:");
        constraintViolations.forEach(violation -> {
          messageLines.add(violation.getPropertyPath().toString() + ": " +
              violation.getMessage() + ", invalid value: " + violation.getInvalidValue());
        });
        Collections.sort(messageLines);
        showMessage(messageLines);
      }
    });

    HorizontalLayout horizontalLayout = new HorizontalLayout();
    horizontalLayout.setPadding(false);
    horizontalLayout.setAlignItems(Alignment.END);
    horizontalLayout.add(billDate, totalValueTextField, addBillItemButton, removeBillItemButton, saveBillButton);

    add(horizontalLayout);
    billItemEditorViews.forEach(this::add);

    setWidth("100%");
    setSpacing(false);
    setPadding(false);
  }

  private void fireSaveNewBillEvent() {
    Broadcaster.broadcast(new Event(SAVE_BILL));
  }

  private void showConfirmation(BillDto billDto) {
    Notification.show("Bill saved: " + billDto, 5000, TOP_CENTER);
  }

  private void showMessage(List<String> messageLines) {
    Notification notification = new Notification();
    messageLines.forEach(line -> {
      Div div = new Div();
      div.setText(line);
      notification.add(div);
    });
    notification.setDuration(5000);
    notification.setPosition(TOP_CENTER);
    notification.open();
  }

  private void clearBillForms() {
    tags = tagService.findAllTagsSorted();
    billItemEditorViews.forEach(this::remove);
    billItemEditorViews.clear();
    IntStream.range(0, DEFAULT_NUMBER_OF_BILL_ITEMS).forEach(i -> billItemEditorViews.add(new BillItemEditorView(tags, this)));
    billItemEditorViews.forEach(this::add);
    totalValueTextField.setValue("0");
  }

  public void recalculateTotal() {
    BigDecimal total = billItemEditorViews.stream()
        .map(BillItemEditorView::readCost)
        .map(BigDecimal::new)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    totalValueTextField.setValue(total.toPlainString());
  }
}
package com.pwawrzyniak.tlog.web.vaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class BIllEditorView extends VerticalLayout {

  private static final int DEFAULT_NUMBER_OF_BILL_ITEMS = 3;
  private static final int MAX_NUMBER_OF_BILL_ITEMS = 10;

  public BIllEditorView(List<String> tags) {
    List<BillItemEditorView> billItemForms = new ArrayList<>();
    IntStream.range(0, DEFAULT_NUMBER_OF_BILL_ITEMS).forEach(i -> billItemForms.add(new BillItemEditorView(tags)));

    DatePicker billDate = new DatePicker();
    billDate.setLabel("Date");

    TextField totalValueTextField = new TextField();
    totalValueTextField.setLabel("Total");
    totalValueTextField.setReadOnly(true);
    totalValueTextField.setTabIndex(-1);

    Button addBillItemButton = new Button("Add bill item", event -> {
      if (billItemForms.size() < MAX_NUMBER_OF_BILL_ITEMS) {
        BillItemEditorView billItemEditorView = new BillItemEditorView(tags);
        billItemForms.add(billItemEditorView);
        this.add(billItemEditorView);
      }
    });
    Button removeBillItemButton = new Button("Remove bill item", event -> {
      if (billItemForms.size() > 1) {
        BillItemEditorView billItemEditorView = billItemForms.get(billItemForms.size() - 1);
        billItemForms.remove(billItemEditorView);
        this.remove(billItemEditorView);
      }
    });
    Button saveBillButton = new Button("Save bill");

    HorizontalLayout horizontalLayout = new HorizontalLayout();
    horizontalLayout.setPadding(false);
    horizontalLayout.setAlignItems(Alignment.END);
    horizontalLayout.add(billDate, totalValueTextField, addBillItemButton, removeBillItemButton, saveBillButton);

    add(horizontalLayout);
    billItemForms.forEach(this::add);

    setWidth("100%");
    setSpacing(false);
    setPadding(false);
  }
}
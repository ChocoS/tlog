package com.pwawrzyniak.tlog.web.vaadin;

import com.pwawrzyniak.tlog.backend.dto.BillItemDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.pwawrzyniak.tlog.web.vaadin.utils.Eval.eval;
import static com.pwawrzyniak.tlog.web.vaadin.utils.ExpressionNormalizer.isNormalized;
import static com.pwawrzyniak.tlog.web.vaadin.utils.ExpressionNormalizer.normalize;

public class BillItemEditorView extends VerticalLayout {

  private BillEditorView billEditorView;
  private Set<String> selectedTags = new HashSet<>();
  private TextField descriptionTextField = new TextField("Description");
  private TextField costTextField = new TextField("Cost");
  private TextField expressionTextField = new TextField("Expression");

  public BillItemEditorView(List<String> tags, BillEditorView billEditorView) {
    this.billEditorView = billEditorView;
    FormLayout selectedTagsComponent = new FormLayout();
    selectedTagsComponent.setWidth("100%");

    expressionTextField.addValueChangeListener(event -> {
      String value = event.getValue();
      if (!isNormalized(value)) {
        value = normalize(value);
        expressionTextField.setValue(value);
      } else {
        costTextField.setValue(eval(value).toPlainString());
        billEditorView.recalculateTotal();
      }
    });

    costTextField.setValue("0");
    costTextField.setReadOnly(true);
    costTextField.setTabIndex(-1);

    descriptionTextField.setWidth("24em");

    ComboBox<String> tagsComboBox = createTagsComboBox(tags, selectedTagsComponent);
    tagsComboBox.setLabel("Tags");

    HorizontalLayout horizontalLayout = new HorizontalLayout();
    horizontalLayout.add(expressionTextField);
    horizontalLayout.add(costTextField);
    horizontalLayout.add(tagsComboBox);
    horizontalLayout.add(descriptionTextField);
    horizontalLayout.setWidth("100%");
    horizontalLayout.setPadding(false);

    add(horizontalLayout);
    add(selectedTagsComponent);

    setWidth("100%");
    setSpacing(false);
    setPadding(false);
  }

  public BillItemDto readBillItemDto() {
    return BillItemDto.builder()
        .cost(costTextField.getValue())
        .description(descriptionTextField.getValue())
        .expression(expressionTextField.getValue())
        .tags(selectedTags)
        .build();
  }

  public String readCost() {
    return costTextField.getValue();
  }

  private ComboBox<String> createTagsComboBox(List<String> tags, FormLayout selectedTagsComponent) {
    ComboBox<String> tagsComboBox = new ComboBox<>();

    tagsComboBox.setItems(tags);
    tagsComboBox.setAllowCustomValue(true);
    tagsComboBox.addCustomValueSetListener(event -> {
      String value = event.getDetail();
      if (!selectedTags.contains(value)) {
        Button tagButton = new Button(value, new Icon(VaadinIcon.CLOSE_SMALL));
        tagButton.setIconAfterText(true);
        tagButton.addClickListener(clicked -> {
          selectedTagsComponent.remove(tagButton);
          selectedTags.remove(value);
        });
        selectedTagsComponent.add(tagButton);
        selectedTags.add(value);
      }
    });
    tagsComboBox.addValueChangeListener(event -> {
      String value = event.getValue();
      if (!event.getSource().isEmpty() && !selectedTags.contains(value)) {
        Button tagButton = new Button(value, new Icon(VaadinIcon.CLOSE_SMALL));
        tagButton.setIconAfterText(true);
        tagButton.addClickListener(clicked -> {
          selectedTagsComponent.remove(tagButton);
          selectedTags.remove(value);
        });
        selectedTagsComponent.add(tagButton);
        selectedTags.add(value);
      }
    });
    return tagsComboBox;
  }
}
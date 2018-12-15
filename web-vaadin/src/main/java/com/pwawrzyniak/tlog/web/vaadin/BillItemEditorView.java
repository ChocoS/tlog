package com.pwawrzyniak.tlog.web.vaadin;

import com.pwawrzyniak.tlog.backend.dto.BillItemDto;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.pwawrzyniak.tlog.backend.validation.ExpressionEvaluator.evaluate;
import static com.pwawrzyniak.tlog.backend.validation.ExpressionEvaluator.isValid;
import static com.pwawrzyniak.tlog.backend.validation.ExpressionNormalizer.isNormalized;
import static com.pwawrzyniak.tlog.backend.validation.ExpressionNormalizer.normalize;
import static com.pwawrzyniak.tlog.backend.validation.ValidationConstants.EXPRESSION_PATTERN;
import static com.pwawrzyniak.tlog.backend.validation.ValidationConstants.MAX_DESCRIPTION_SIZE;
import static com.pwawrzyniak.tlog.backend.validation.ValidationConstants.MAX_EXPRESSION_SIZE;
import static com.pwawrzyniak.tlog.backend.validation.ValidationConstants.MAX_TAG_SIZE;

public class BillItemEditorView extends VerticalLayout {

  private Set<String> selectedTags = new HashSet<>();
  private TextField descriptionTextField = new TextField("Description");
  private TextField costTextField = new TextField("Cost");
  private TextField expressionTextField = new TextField("Expression");
  private ComboBox<String> tagsComboBox;

  public BillItemEditorView(List<String> tags, BillEditorView billEditorView) {
    Div selectedTagsComponent = new Div();

    expressionTextField.addValueChangeListener(event -> {
      String value = event.getValue();
      if (!isNormalized(value)) {
        value = normalize(value);
        expressionTextField.setValue(value);
      } else {
        costTextField.setValue(evaluate(value).toPlainString());
        billEditorView.recalculateTotal();
      }
    });
    expressionTextField.addFocusListener(event -> expressionTextField.setInvalid(false));
    expressionTextField.addBlurListener(event ->
        expressionTextField.setInvalid(!isValid(expressionTextField.getValue())));
    expressionTextField.setPattern(EXPRESSION_PATTERN);
    expressionTextField.setPreventInvalidInput(true);
    expressionTextField.setErrorMessage("Invalid expression");
    expressionTextField.setMaxLength(MAX_EXPRESSION_SIZE);

    costTextField.setValue("0");
    costTextField.setReadOnly(true);
    costTextField.setTabIndex(-1);

    descriptionTextField.setWidth("24em");
    descriptionTextField.setMaxLength(MAX_DESCRIPTION_SIZE);
    descriptionTextField.setErrorMessage("Description is too long");

    tagsComboBox = createTagsComboBox(tags, selectedTagsComponent);
    tagsComboBox.setLabel("Tags");

    HorizontalLayout horizontalLayout = new HorizontalLayout();
    horizontalLayout.add(expressionTextField);
    horizontalLayout.add(costTextField);
    horizontalLayout.add(tagsComboBox);
    horizontalLayout.add(descriptionTextField);
    horizontalLayout.setWidth("100%");
    horizontalLayout.setPadding(false);
    horizontalLayout.setAlignItems(Alignment.START);

    add(horizontalLayout);
    add(selectedTagsComponent);

    setWidth("100%");
    setSpacing(false);
    setPadding(false);
  }

  public BillItemDto readBillItemDto() {
    String expression = expressionTextField.getValue();
    String description = descriptionTextField.getValue();
    if (isNullOrBlank(expression) && isNullOrBlank(description) && selectedTags.isEmpty()) {
      return null;
    }
    return BillItemDto.builder()
        .cost(costTextField.getValue())
        .description(descriptionTextField.getValue())
        .expression(expression)
        .tags(selectedTags)
        .build();
  }

  private boolean isNullOrBlank(String string) {
    return string == null || string.isBlank();
  }

  public String readCost() {
    return costTextField.getValue();
  }

  private ComboBox<String> createTagsComboBox(List<String> tags, HasComponents selectedTagsComponent) {
    ComboBox<String> tagsComboBox = new ComboBox<>();

    tagsComboBox.setItems(tags);
    tagsComboBox.setAllowCustomValue(true);
    tagsComboBox.setErrorMessage("Tag is too long");
    tagsComboBox.addFocusListener(event -> tagsComboBox.setInvalid(false));
    tagsComboBox.addCustomValueSetListener(event -> {
      String value = event.getDetail().toLowerCase();
      if (value.length() > MAX_TAG_SIZE) {
        tagsComboBox.setInvalid(true);
      } else if (!selectedTags.contains(value)) {
        tagsComboBox.setInvalid(false);
        addButton(selectedTagsComponent, value);
      }
    });
    tagsComboBox.addValueChangeListener(event -> {
      tagsComboBox.setInvalid(false);
      if (event.getSource().isEmpty()) {
        return;
      }
      String value = event.getValue().toLowerCase();
      if (!selectedTags.contains(value)) {
        addButton(selectedTagsComponent, value);
      }
    });
    return tagsComboBox;
  }

  private void addButton(HasComponents selectedTagsComponent, String value) {
    Button tagButton = new Button(value, new Icon(VaadinIcon.CLOSE_SMALL));
    tagButton.setIconAfterText(true);
    tagButton.addClickListener(clicked -> {
      selectedTagsComponent.remove(tagButton);
      selectedTags.remove(value);
    });
    tagButton.getStyle().set("margin-right", "5px");
    selectedTagsComponent.add(tagButton);
    selectedTags.add(value);
  }

  public void display(BillItemDto billItemDto) {
    expressionTextField.setValue(billItemDto.getExpression());
    billItemDto.getTags().forEach(tag -> tagsComboBox.setValue(tag));
    descriptionTextField.setValue(billItemDto.getDescription() != null ? billItemDto.getDescription() : "");
  }
}
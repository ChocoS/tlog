package com.pwawrzyniak.tlog.web.vaadin;

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

public class BillItemEditorView extends VerticalLayout {

  public BillItemEditorView(List<String> tags) {
    FormLayout selectedTagsComponent = new FormLayout();
    selectedTagsComponent.setWidth("100%");

    TextField expressionTextField = new TextField();
    expressionTextField.setLabel("Expression");

    TextField valueTextField = new TextField();
    valueTextField.setLabel("Value");
    valueTextField.setReadOnly(true);
    valueTextField.setTabIndex(-1);

    TextField descriptionTextField = new TextField();
    descriptionTextField.setLabel("Description");
    descriptionTextField.setWidth("24em");

    ComboBox<String> tagsComboBox = createTagsComboBox(tags, selectedTagsComponent);
    tagsComboBox.setLabel("Tags");

    HorizontalLayout horizontalLayout = new HorizontalLayout();
    horizontalLayout.add(expressionTextField);
    horizontalLayout.add(valueTextField);
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

  private ComboBox<String> createTagsComboBox(List<String> tags, FormLayout selectedTagsComponent) {
    ComboBox<String> tagsComboBox = new ComboBox<>();
    Set<String> selectedTags = new HashSet<>();

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
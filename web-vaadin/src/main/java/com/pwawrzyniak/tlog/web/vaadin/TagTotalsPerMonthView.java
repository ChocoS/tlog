package com.pwawrzyniak.tlog.web.vaadin;

import com.pwawrzyniak.tlog.backend.dto.TagTotalsPerMonthDto;
import com.pwawrzyniak.tlog.backend.service.BillService;
import com.pwawrzyniak.tlog.backend.service.TagService;
import com.pwawrzyniak.tlog.web.vaadin.events.Broadcaster;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;

@SpringComponent
@UIScope
public class TagTotalsPerMonthView extends VerticalLayout { // wrapping layout is needed to mitigate grid height issue

  private Grid<TagTotalsPerMonthDto> tagTotalsGrid;
  private Registration broadcasterRegistration;

  public TagTotalsPerMonthView(@Autowired BillService billService, @Autowired TagService tagService) {
    tagTotalsGrid = new Grid<>();
    tagTotalsGrid.setDataProvider(DataProvider.fromCallbacks(
        query -> billService.getTagTotalsPerMonthList(query.getOffset(), query.getLimit()).stream(),
        query -> billService.getMonthCount()
    ));

    tagTotalsGrid.addColumn(new LocalDateRenderer<>(TagTotalsPerMonthDto::getDate, DateTimeFormatter.ofPattern("uuuu-MM")))
        .setHeader("Date").setFlexGrow(0).setWidth("100px");
    tagService.findAllTagsSorted().forEach(tag -> tagTotalsGrid.addColumn(new TagCostValueProvider(tag))
        .setHeader(tag).setFlexGrow(0).setWidth("150px"));

    tagTotalsGrid.getStyle().set("border", "1px solid gray");
    tagTotalsGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COMPACT);
    tagTotalsGrid.setSelectionMode(Grid.SelectionMode.NONE);

    add(tagTotalsGrid);

    setPadding(false);
    setSpacing(false);

    refreshData();
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    UI ui = attachEvent.getUI();
    broadcasterRegistration = Broadcaster.register(myEvent -> ui.access(this::refreshData));
  }

  @Override
  protected void onDetach(DetachEvent detachEvent) {
    broadcasterRegistration.remove();
    broadcasterRegistration = null;
  }

  private void refreshData() {
    tagTotalsGrid.getDataProvider().refreshAll();
  }

  static class TagCostValueProvider implements ValueProvider<TagTotalsPerMonthDto, String> {

    private final String tag;

    TagCostValueProvider(String tag) {
      this.tag = tag;
    }

    @Override
    public String apply(TagTotalsPerMonthDto tagTotalsPerMonthDto) {
      return tagTotalsPerMonthDto.getTagTotalMap().get(tag);
    }
  }
}
package com.escod.untitled.mlapp.view;

import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import io.jmix.core.MessageTools;
import io.jmix.core.metamodel.model.MetaClass;
import io.jmix.core.metamodel.model.MetaProperty;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.ComponentGenerationContext;
import io.jmix.flowui.component.UiComponentsGenerator;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.data.SupportsValueSource;
import io.jmix.flowui.data.value.ContainerValueSource;
import io.jmix.flowui.model.DataComponents;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.StandardListView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import io.jmix.core.Metadata;

public abstract class MalaitaStandardListView<E> extends StandardListView<E> implements DetailRender<E> {

  public static final String FIELD = "Field";

  @Autowired
  protected MessageTools messageTools;

  @Autowired
  protected DataComponents dataComponents;

  @Autowired
  protected Metadata metadata;

  @Autowired
  protected UiComponentsGenerator componentsGenerator;

  @Autowired
  protected UiComponents uiComponents;

  private MetaClass metaClass;

  @ViewComponent
  protected DataGrid<E> eDataGrid;

  @Subscribe
  protected void onInit(InitEvent event) {
    if (metaClass == null) {
      metaClass = metadata.getClass(this.getEntityClass());
    }

    eDataGrid.setItemDetailsRenderer(createComponentDetailRender());
  }

  protected FormLayout generateDetailRender(String minWidth, int columns, String... properties) {

    FormLayout formLayout = uiComponents.create(FormLayout.class);
    formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep(minWidth, columns));

    for (String prop : properties) {
      generateComponent(formLayout, metaClass.getProperty(prop));
    }

    return formLayout;
  }

  protected void generateComponent(FormLayout formLayout, MetaProperty metaProperty) {
    ComponentGenerationContext componentGenerationContext = new ComponentGenerationContext(metaClass, metaProperty.getName());

    com.vaadin.flow.component.Component generatedComponent =
        componentsGenerator.generate(componentGenerationContext);
    generatedComponent.setId(metaProperty.getName() + FIELD);

    formLayout.add(generatedComponent);
  }

  protected ComponentRenderer<FormLayout, E> createComponentDetailRender() {
    return new ComponentRenderer<>(this::createDetailRender, this::setItem);
  }

  protected void setValueToComponent(com.vaadin.flow.component.Component component,
                                     InstanceContainer<E> instanceContainer) {
    if (component.getId().isEmpty()) {
      return;
    }

    String id = component.getId().get();
    String propertyName = id.replace(FIELD, StringUtils.EMPTY);

    if (component instanceof SupportsValueSource<?> valueSourceComponent) {
      valueSourceComponent.setValueSource(new ContainerValueSource<>(instanceContainer, propertyName));
    }

    if (component instanceof HasLabel hasLabelComponent) {
      MetaClass metaClass = instanceContainer.getEntityMetaClass();
      hasLabelComponent.setLabel(messageTools.getPropertyCaption(metaClass, propertyName));
    }

    if (component instanceof HasValue<?, ?> hasValueComponent) {
      // TODO validate if is read only
      hasValueComponent.setReadOnly(true);
    }
  }
}

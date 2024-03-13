package com.escod.untitled.mlapp.view;

import com.vaadin.flow.component.formlayout.FormLayout;

public interface DetailRender<ITEM> {
  FormLayout createDetailRender();
  void setItem(FormLayout formLayout, ITEM item);
  Class<ITEM> getEntityClass();
}

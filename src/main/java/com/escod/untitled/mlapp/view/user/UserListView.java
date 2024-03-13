package com.escod.untitled.mlapp.view.user;

import com.escod.untitled.mlapp.entity.User;
import com.escod.untitled.mlapp.view.MalaitaStandardListView;
import com.escod.untitled.mlapp.view.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;

@Route(value = "users", layout = MainView.class)
@ViewController("mlapp_User.list")
@ViewDescriptor("user-list-view.xml")
@LookupComponent("eDataGrid")
@DialogMode(width = "64em")
public class UserListView extends MalaitaStandardListView<User> {

  @ViewComponent("sectionDetail")
  private Component sectionDetail;

  @Override
  public FormLayout createDetailRender() {
    FormLayout formLayout = uiComponents.create(FormLayout.class);
    formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 3));

    String[] props = {"username", "firstName", "lastName", "email", "active" };
    return generateDetailRender("0", 2, props);
  }

  @Override
  public void setItem(FormLayout formLayout, User user) {
    InstanceContainer<User> instanceContainer = dataComponents.createInstanceContainer(User.class);
    instanceContainer.setItem(user);
    formLayout.getChildren().forEach(component -> setValueToComponent(component, instanceContainer));
    sectionDetail.setVisible(true);
    setValueToComponent(sectionDetail, instanceContainer);
  }

  @Override
  public Class<User> getEntityClass() {
    return User.class;
  }
}
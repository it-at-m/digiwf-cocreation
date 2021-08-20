package io.miragon.bpmrepo.spring.boot.starter.menu;

import io.miragon.bpmrepo.core.menu.api.plugin.MenuPlugin;
import io.miragon.bpmrepo.core.menu.api.transport.MenuItemTO;

import java.util.ArrayList;
import java.util.List;

public class DefaultMenuPlugin implements MenuPlugin {

    @Override
    public List<MenuItemTO> getMenuItems() {

        //Define the URL where the tools are hosted
        //Pass a List of FileTypes, which can be opened by the corresponding tool
        // -> The name of the filetypes matches the names from the DefaultFileTypesPlugin

        final List<MenuItemTO> menuItems = new ArrayList<>();
        MenuItemTO item = new MenuItemTO("Home", "", "folder", 1);
        menuItems.add(item);
        item = new MenuItemTO("Forms", "/formulare", "view_quilt", 2);
        menuItems.add(item);
        item = new MenuItemTO("Integration", "/bausteine", "widgets", 3);
        menuItems.add(item);
        item = new MenuItemTO("Modeller", "https://modeler.miragon.cloud/#/", "web", 4);
        menuItems.add(item);
        item = new MenuItemTO("Templates", "localhost:8080/templateBuilder", "dashboard", 4);
        menuItems.add(item);
        return menuItems;
    }
}

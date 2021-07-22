package io.miragon.bpmrepo.core.menu.api.plugin;

import io.miragon.bpmrepo.core.menu.api.transport.MenuItemTO;

import java.util.List;

public interface MenuPlugin {

    List<MenuItemTO> getMenuItems();

}

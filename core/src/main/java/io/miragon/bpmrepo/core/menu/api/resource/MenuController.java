package io.miragon.bpmrepo.core.menu.api.resource;

import io.miragon.bpmrepo.core.menu.api.plugin.MenuPlugin;
import io.miragon.bpmrepo.core.menu.api.transport.MenuItemTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Validated
@Transactional
@RestController
@RequiredArgsConstructor
@Tag(name = "Menu")
@RequestMapping("/api/menu")
public class MenuController {

    private final MenuPlugin menuPlugin;

    /**
     * Get all menu items.
     *
     * @return menu items
     */
    @GetMapping()
    public ResponseEntity<List<MenuItemTO>> getAllMenuItems() {
        log.debug("Get all Menu Items");
        val menuItems = this.menuPlugin.getMenuItems();
        return ResponseEntity.ok(menuItems);
    }

}

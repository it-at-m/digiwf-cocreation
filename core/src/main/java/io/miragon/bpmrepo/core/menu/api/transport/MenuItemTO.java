package io.miragon.bpmrepo.core.menu.api.transport;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemTO {

    private String name;

    private String url;

    private String icon;

    private Integer position;

}

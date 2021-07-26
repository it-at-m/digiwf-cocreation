package io.miragon.bpmrepo.core.menu.api.transport;

import lombok.*;

import java.util.List;

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

    private List<String> fileTypes;

}

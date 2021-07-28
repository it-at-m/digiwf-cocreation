package io.miragon.bpmrepo.core.menu.api.transport;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemTO {

    @NotNull
    private String name;

    @NotNull
    private String url;

    @NotNull
    private String icon;
    
    private Integer position;

}

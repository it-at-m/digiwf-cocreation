package io.miragon.bpmrepo.core.menu.api.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Containing the specifications for connected apps")
public class MenuItemTO {

    @NotNull
    private String name;

    @NotNull
    private String url;

    @NotNull
    private String icon;

    private Integer position;

}

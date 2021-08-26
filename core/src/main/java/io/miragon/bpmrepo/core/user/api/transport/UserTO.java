package io.miragon.bpmrepo.core.user.api.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Simplified user object only containing username")
public class UserTO {

    @NotBlank
    @Size(min = 3, max = 50)
    private String userName;

}

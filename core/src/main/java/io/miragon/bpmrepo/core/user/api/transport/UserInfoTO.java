package io.miragon.bpmrepo.core.user.api.transport;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoTO {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @Nullable
    private String id;

}

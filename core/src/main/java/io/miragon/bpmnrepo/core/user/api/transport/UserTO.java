package io.miragon.bpmnrepo.core.user.api.transport;

import com.sun.istack.Nullable;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTO {

    @NotBlank
    @Size(min = 3, max = 50)
    private String userName;

}

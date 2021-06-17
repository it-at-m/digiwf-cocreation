package io.miragon.bpmnrepo.core.user.api.transport;

import com.sun.istack.Nullable;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateTO {

    @NotEmpty
    private String userId;

    @Nullable
    @Size(min = 3, max = 50)
    private String username;
}

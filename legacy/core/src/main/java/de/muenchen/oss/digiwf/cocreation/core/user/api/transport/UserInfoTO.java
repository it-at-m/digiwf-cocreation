package de.muenchen.oss.digiwf.cocreation.core.user.api.transport;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Containing all information about an user")
public class UserInfoTO {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    private String id;

}

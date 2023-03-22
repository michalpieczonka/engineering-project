package pm.workout.helper.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pm.workout.helper.domain.user.UserAuthority;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppUserDto {
    private Long userId;
    private String username;
    private String password;
    private String email;
    UserAuthority authority;
}

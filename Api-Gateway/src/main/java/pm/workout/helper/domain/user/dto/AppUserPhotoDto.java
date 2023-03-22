package pm.workout.helper.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pm.workout.helper.api.JsonLocalDateTimeSerializer;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserPhotoDto {
    private long photoId;
    private String name;
    @JsonLocalDateTimeSerializer
    private LocalDateTime uploadedTime;
    private String type;
    private byte[] imageData;
}

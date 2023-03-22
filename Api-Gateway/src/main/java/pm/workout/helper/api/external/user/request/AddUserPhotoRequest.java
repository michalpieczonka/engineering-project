package pm.workout.helper.api.external.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddUserPhotoRequest {
    String contentType;
    String fileName;
    byte[] file;
}
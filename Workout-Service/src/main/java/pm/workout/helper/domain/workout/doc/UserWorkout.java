package pm.workout.helper.domain.workout.doc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "usersWorkouts")
public class UserWorkout {
    @Id
    private String userId;
    private List<Workout> performedWorkouts;
}

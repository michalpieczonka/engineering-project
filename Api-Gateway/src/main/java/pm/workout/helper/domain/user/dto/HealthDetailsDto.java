package pm.workout.helper.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pm.workout.helper.api.JsonLocalDateSerializer;
import pm.workout.helper.api.JsonLocalDateTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthDetailsDto {
    private double weight;
    private double height;
    private double waistCircuit; //pas
    private double waistCircumference; //talia
    private double armCircumference;
    private double thighCircumference;
    @JsonLocalDateTimeSerializer
    private LocalDateTime latestUpdatedTime;
    @JsonLocalDateSerializer
    private LocalDate trainingStartDate;

}

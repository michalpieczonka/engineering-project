package pm.workout.helper.api.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class UpdateUserHealthDetailsRequest {
    @NotNull
    @Positive
    private double weight;
    @NotNull
    @Positive
    private double height;
    @NotNull
    @Positive
    private double waistCircuit; //pas
    @NotNull
    @Positive
    private double waistCircumference; //talia
    @NotNull
    @Positive
    private double armCircumference;
    @NotNull
    @Positive
    private double thighCircumference;
}

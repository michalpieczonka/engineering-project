package pm.workout.helper.domain.training.plan;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pm.workout.helper.domain.training.plan.dto.TrainingPlanDetailsDto;
import pm.workout.helper.domain.training.plan.dto.TrainingUnitDto;
import pm.workout.helper.domain.training.plan.template.SeriesRepetitionsTemplate;
import pm.workout.helper.domain.training.plan.template.TrainingUnitPartTemplate;
import pm.workout.helper.domain.training.plan.template.TrainingUnitTemplate;
import pm.workout.helper.domain.workout.dto.SeriesRepetitionsDetails;
import pm.workout.helper.domain.workout.dto.UserWorkoutDto;
import pm.workout.helper.domain.workout.dto.WorkoutPartDto;
import pm.workout.helper.infrastructure.services.TrainingPlanConfigurationService;
import pm.workout.helper.infrastructure.services.WorkoutService;

import java.util.List;

@Service
@AllArgsConstructor
class TrainingPlanServiceImpl implements TrainingPlanService {
    private final TrainingPlanConfigurationService trainingPlanConfigurationService;
    private final WorkoutService workoutService;

    @Override
    public TrainingUnitTemplate getTrainingUnitTemplate(long userId, long trainingPlanId, long trainingUnitId) {
        TrainingUnitDto trainingUnit = trainingPlanConfigurationService.getTrainingPlan(trainingPlanId)
                .getTrainingUnits()
                .stream()
                .filter(unit -> unit.getId() == trainingUnitId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Training unit not found"));

        UserWorkoutDto latestUserWorkout = workoutService.getLatestUserWorkout(userId, trainingUnitId);
        if (latestUserWorkout.getWorkoutParts().isEmpty()){
            List<TrainingUnitPartTemplate> trainingUnitParts = trainingUnit.getTrainingUnitParts()
                    .stream()
                    .map(unit ->
                            new TrainingUnitPartTemplate(unit.getId(), unit.getExerciseId(), unit.getExerciseName(),
                                    unit.getSeriesRepetitionsDetails()
                                            .stream()
                                            .map(part -> new SeriesRepetitionsTemplate(part.getSeriesNumber(), part.getRepetitionsNumber(), 0, 0))
                                            .toList()))
                    .toList();
            return new TrainingUnitTemplate(trainingPlanId, trainingUnitId, trainingUnit.getTrainingDay(), trainingUnitParts);
        }else {
            List<TrainingUnitPartTemplate> trainingUnitParts = trainingUnit.getTrainingUnitParts().stream().map(unit -> {
                WorkoutPartDto workoutPart = latestUserWorkout.getWorkoutParts().stream().filter(workout -> workout.getTrainingUnitPartId() == unit.getId()).findFirst().orElseThrow(() -> new RuntimeException("Workout part not found"));
                return new TrainingUnitPartTemplate(unit.getId(), unit.getExerciseId(), unit.getExerciseName(), unit.getSeriesRepetitionsDetails().stream().map(uSeries -> {
                    SeriesRepetitionsDetails performed =  workoutPart.getSeriesRepetitionsDetails().stream().filter(wSeries -> wSeries.getSeriesNumber() == uSeries.getSeriesNumber()).findFirst().orElseThrow(() -> new RuntimeException("Series number not found"));
                    return new SeriesRepetitionsTemplate(uSeries.getSeriesNumber(), uSeries.getRepetitionsNumber(), performed.getPerformedRepetitionsNumber(), performed.getUsedWeight());
                }).toList());
            }).toList();
            return new TrainingUnitTemplate(
                    trainingPlanId,
                    trainingUnitId,
                    trainingUnit.getTrainingDay(),
                    trainingUnitParts
            );
        }



    }
}

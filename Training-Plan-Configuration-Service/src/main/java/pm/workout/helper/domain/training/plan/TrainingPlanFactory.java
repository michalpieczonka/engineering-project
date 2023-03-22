package pm.workout.helper.domain.training.plan;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pm.workout.helper.api.training.plan.request.CreateTrainingPlanRequest;
import pm.workout.helper.api.training.plan.request.CreateTrainingUnitPartRequest;
import pm.workout.helper.api.training.plan.request.CreateTrainingUnitRequest;
import pm.workout.helper.api.training.plan.request.plan.AddTrainingDayRequest;
import pm.workout.helper.domain.training.excercise.Exercise;
import pm.workout.helper.domain.training.excercise.ExerciseRepository;
import pm.workout.helper.domain.training.excercise.exception.ExerciseNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
class TrainingPlanFactory {

    private final ExerciseRepository exerciseRepository;

    public TrainingUnit buildTrainingUnit(TrainingPlan plan, AddTrainingDayRequest request){
        TrainingUnit trainingUnit = TrainingUnit.builder().trainingDay(request.getTrainingDay())
                .trainingPlan(plan).build();
        request.getTrainingUnitParts().forEach(trainingUnitPart -> {
            Exercise exercise = exerciseRepository.getExerciseById(trainingUnitPart.getExerciseId())
                    .orElseThrow(() -> new ExerciseNotFoundException(trainingUnitPart.getExerciseId()));

            TrainingUnitPart trUnitPart = TrainingUnitPart.builder()
                    .exercise(exercise)
                    .build();

            trainingUnitPart.getSeriesRepetitionsDetails().forEach(d -> trUnitPart.addSeriesRepetitions(d.getSeriesNumber(), d.getRepetitionsNumber()));
            trainingUnit.addTrainingPart(trUnitPart);
        });
        return trainingUnit;
    }

    //todo:  wyniesc jako metoda i wykorzystac ponownie
    public TrainingUnitPart buildTrainingUnitPart(TrainingUnit trainingUnit, CreateTrainingUnitPartRequest request){
        Exercise exercise = exerciseRepository.getExerciseById(request.getExerciseId())
                .orElseThrow(() -> new ExerciseNotFoundException(request.getExerciseId()));

        TrainingUnitPart trUnitPart = TrainingUnitPart.builder()
                .exercise(exercise)
                .build();

        request.getSeriesRepetitionsDetails().forEach(d -> trUnitPart.addSeriesRepetitions(d.getSeriesNumber(), d.getRepetitionsNumber()));
        trainingUnit.addTrainingPart(trUnitPart);
        return trUnitPart;
    }

    public TrainingPlan buildAndValidateTrainingPlan(CreateTrainingPlanRequest details, Long userCreatorId) {
        TrainingPlan trainingPlan = buildTrainingPlan(details);
        trainingPlan.setPlanOwner(userCreatorId);
        trainingPlan.setTargetFinishDate(details.getTargetFinishDate());

        Map<TrainingDay, List<CreateTrainingUnitRequest>> unitsByTrainingDay = splitTrainingUnitsByTrainingDay(details.getTrainingUnitsDetails());

        unitsByTrainingDay.forEach((x, y) -> {
            TrainingUnit trainingUnit = TrainingUnit.builder().trainingDay(x).trainingPlan(trainingPlan).build();
            y.forEach(requestDetails -> {
                requestDetails.getTrainingUnitParts().forEach(trainingUnitPart -> {
                    Exercise exercise = exerciseRepository.getExerciseById(trainingUnitPart.getExerciseId())
                            .orElseThrow(() -> new ExerciseNotFoundException(trainingUnitPart.getExerciseId()));

                    TrainingUnitPart trUnitPart = TrainingUnitPart.builder()
                            .exercise(exercise)
                            .build();

                    trainingUnitPart.getSeriesRepetitionsDetails().forEach(d -> trUnitPart.addSeriesRepetitions(d.getSeriesNumber(), d.getRepetitionsNumber()));
                    trainingUnit.addTrainingPart(trUnitPart);
                });
            });
            trainingPlan.addTrainingUnit(trainingUnit);
        });

        return trainingPlan;
    }

    private TrainingPlan buildTrainingPlan(CreateTrainingPlanRequest details) {
        return TrainingPlan.builder()
                .title(details.getTitle())
                .description(details.getDescription())
                .planPriority(details.getPlanPriority())
                .type(details.getPlanType())
                .isPublic(details.isPublic())
                .numberOfTrainingDays(details.getTrainingDays().size())
                .creationDate(LocalDateTime.now())
                .latestModificationDate(LocalDateTime.now())
                .build();
    }

    private Map<TrainingDay, List<CreateTrainingUnitRequest>> splitTrainingUnitsByTrainingDay(List<CreateTrainingUnitRequest> details) {
        return details.stream()
                .collect(Collectors.groupingBy(CreateTrainingUnitRequest::getTrainingDay));
    }

}

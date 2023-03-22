package pm.workout.helper.domain.training.plan;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pm.workout.helper.api.training.plan.request.CreateTrainingPlanRequest;
import pm.workout.helper.domain.training.plan.dto.PublicTrainingPlanDto;
import pm.workout.helper.domain.training.plan.dto.SeriesRepetitionsDto;
import pm.workout.helper.domain.training.plan.dto.TrainingPlanDetailsDto;
import pm.workout.helper.domain.training.plan.dto.TrainingPlanDto;
import pm.workout.helper.domain.training.plan.dto.TrainingUnitDto;
import pm.workout.helper.domain.training.plan.dto.TrainingUnitPartDto;
import pm.workout.helper.domain.training.plan.dto.template.TrainingUnitPartTemplateDto;
import pm.workout.helper.domain.training.plan.dto.template.TrainingUnitTemplateDto;
import pm.workout.helper.domain.training.plan.rate.TrainingPlanRate;
import pm.workout.helper.domain.training.plan.rate.dto.TrainingPlanRateDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
interface TrainingPlanMapper {
    @Mapping(source = "request.planType", target = "type")
    TrainingPlan toDomain(CreateTrainingPlanRequest request);


    @Named("mapTrainingPlanRatesToDto")
    default Set<TrainingPlanRateDto> mapTrainingPlanRatesToDto (Set<TrainingPlanRate> planRates){
        return planRates.stream().map(rate -> new TrainingPlanRateDto(rate.getId(), rate.getDescription(), rate.getRate(), rate.getRateDate()))
                .collect(Collectors.toSet());
    }

    @Mapping(source = "trainingPlan.trainingUnits", target = "trainingUnits", qualifiedByName = "mapTrainingUnitsToDto")
    @Mapping(source = "trainingPlan", target = "trainingDays", qualifiedByName = "mapTrainingDays")
    @Mapping(source = "trainingPlan.description", target = "planDescription")
    @Mapping(source = "trainingPlan.type", target = "planType")
    @Mapping(source = "trainingPlan.planRates", target = "planRates", qualifiedByName = "mapTrainingPlanRatesToDto")
    TrainingPlanDetailsDto toDetailsDto(TrainingPlan trainingPlan);

    @Named("mapTrainingUnitsToDto")
    default Set<TrainingUnitDto> mapTrainingUnitsToDto(Set<TrainingUnit> trainingUnits) {
        return trainingUnits.stream()
                .map(u -> new TrainingUnitDto(u.getId(), u.getTrainingDay(),
                                u.getUnitParts().stream()
                                        .map(part -> new TrainingUnitPartDto(part.getId(), part.getExercise().getId(), part.getExercise().getName(),
                                                part.getExercise().getTargetMuscle(), part.getExercise().getAdditionalMuscles(),
                                                part.getSeriesRepetitionsMap().entrySet().stream()
                                                        .map(entry -> new SeriesRepetitionsDto(entry.getKey(), entry.getValue()))
                                                        .collect(Collectors.toSet())))
                                        .collect(Collectors.toSet())))
                .collect(Collectors.toSet());
    }

    @Named("mapTrainingDays")
    default Set<TrainingDay> mapTrainingDays (TrainingPlan trainingplan){
        return trainingplan.getTrainingUnits().stream().map(TrainingUnit::getTrainingDay)
                .collect(Collectors.toSet());
    }


    TrainingPlanDto toDto(TrainingPlan trainingPlan);

    @Named("mapAverageRate")
    default double mapAverageRate (TrainingPlan trainingPlan){
        return trainingPlan.getPlanRate();
    }

    @Named("mapIsInUserPlans")
    default boolean mapIsInUserPlans (TrainingPlan trainingPlan, long userId){
        return trainingPlan.getPlanUsersIds().contains(userId);
    }

    @Named("mapIsCreatedByUser")
    default boolean mapIsCreatedByUser (TrainingPlan trainingPlan, long userId){
        return trainingPlan.getPlanCreatorUserId().equals(userId);
    }

    default PublicTrainingPlanDto toPublicDto(TrainingPlan plan, Long requestedByUserId){
        return new PublicTrainingPlanDto(plan.getId(),
                plan.getTitle(), plan.getDescription(), plan.getNumberOfTrainingDays(), plan.getPlanPriority(), plan.isPublic(), plan.getType(),
                plan.getPreferredTrainingInternship(), plan.getPlanCreatorUserId(),
                plan.getPlanUsersIds(), plan.getCreationDate(), plan.getPlanRates().stream().map(rate -> new TrainingPlanRateDto(rate.getId(), rate.getDescription(),
                rate.getRate(), rate.getRateDate())).collect(Collectors.toSet()),
                plan.getPlanRate(), plan.getPlanUsersIds().contains(requestedByUserId), plan.getPlanCreatorUserId().equals(requestedByUserId));

    }

   default TrainingUnitTemplateDto mapToTemplate(Long trainingPlanId, TrainingUnit trainingUnit) {
        return new TrainingUnitTemplateDto(trainingPlanId, trainingUnit.getId(), trainingUnit.getTrainingDay(),
                trainingUnit.getUnitParts()
                        .stream().map(part -> new TrainingUnitPartTemplateDto(
                                part.getExercise().getId(),
                                part.getExercise().getName(),
                                part.getExercise().getDescription(),
                                part.getExercise().getVideoUrl(),
                                part.getExercise().getTargetMuscle(),
                                part.getSeriesRepetitionsMap()
                                        .entrySet()
                                        .stream()
                                        .map(entry -> new SeriesRepetitionsDto(entry.getKey(), entry.getValue())
                                        )
                                        .collect(Collectors.toSet())))
                        .collect(Collectors.toSet()));
    }
}

package pm.workout.helper.domain.training.excercise;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pm.workout.helper.domain.training.excercise.dto.ExerciseDto;

@Mapper(componentModel = "spring")
interface ExerciseMapper {
    @Mapping(source = "exercise.id", target = "exerciseId")
    @Mapping(source = "exercise.name", target = "exerciseName")
    @Mapping(source = "exercise.description", target = "exerciseDescription")
    @Mapping(source = "exercise.additionalMuscles", target="additionalMuscles")
    ExerciseDto toDto(Exercise exercise);
}

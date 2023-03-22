package pm.workout.helper.domain.training.excercise

import org.mapstruct.factory.Mappers
import pm.workout.helper.api.training.exercise.request.CreateExerciseRequest
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicLong

class ExerciseServiceImplTest extends Specification {
    def "when trying to save exercise should save"() {
        given:
        def exerciseRepository = new ExerciseRepositoryMock()
        def exerciseMapper = Mappers.getMapper(ExerciseMapper.class)
        def exerciseService = new ExerciseServiceImpl(exerciseRepository, exerciseMapper)
        def request = new CreateExerciseRequest("test", "desc", "url", ExerciseType.COMPOUNDED, TargetMuscle.CHEST, List.of())

        when:
        def savedExercise = exerciseService.addExercise(request)

        then:
        exerciseRepository.getAllExercises().get(0).name == "test"
        exerciseRepository.getAllExercises().get(0).description == "desc"
        exerciseRepository.getAllExercises().get(0).videoUrl == "url"
        exerciseRepository.getAllExercises().get(0).exerciseType == ExerciseType.COMPOUNDED
        exerciseRepository.getAllExercises().get(0).targetMuscle == TargetMuscle.CHEST
    }

    def "when trying to get exercise by target muscle should return valid exercises"() {
        given:
        def ex1 = buildExercise("toFound", TargetMuscle.CHEST)
        def ex2 = buildExercise("notToFound", TargetMuscle.ABDUCTORS)
        def exerciseRepository = new ExerciseRepositoryMock(List.of(ex1, ex2))
        def exerciseMapper = Mappers.getMapper(ExerciseMapper.class)
        def exerciseService = new ExerciseServiceImpl(exerciseRepository, exerciseMapper)

        when:
        def response = exerciseService.getExercisesByTargetMuscleGroup(TargetMuscle.CHEST)

        then:
        response.size() == 1
        response.get(0).exerciseName == "toFound"
        response.get(0).targetMuscle == TargetMuscle.CHEST
    }

    private Exercise buildExercise(String name, TargetMuscle targetMuscle) {
        return Exercise
                .builder()
                .name(name)
                .targetMuscle(targetMuscle)
                .description("desc")
                .videoUrl("url")
                .additionalMuscles(Set.of())
                .build()
    }

    class ExerciseRepositoryMock implements ExerciseRepository {
        private final Map<Long, Exercise> EXERCISES_MAP = new HashMap<>();
        private AtomicLong seq = new AtomicLong(1L)

        ExerciseRepositoryMock() {}

        ExerciseRepositoryMock(List<Exercise> exercises) {
            exercises.each { EXERCISES_MAP.put(seq.getAndIncrement(), it) }
        }

        @Override
        List<Exercise> getAllExercises() {
            return EXERCISES_MAP.values().stream().toList();
        }

        @Override
        List<Exercise> getAllByMuscleGroup(TargetMuscle targetMuscle) {
            return EXERCISES_MAP.values().stream().filter { it.targetMuscle == targetMuscle }.toList();
        }

        @SuppressWarnings("GroovyAccessibility")
        @Override
        void addExercise(Exercise exercise) {
            exercise.id = seq.getAndIncrement()
            EXERCISES_MAP.put(exercise.id, exercise)
        }

        @Override
        Optional<Exercise> getExerciseById(Long id) {
            return Optional.ofNullable(EXERCISES_MAP.get(id))
        }
    }
}

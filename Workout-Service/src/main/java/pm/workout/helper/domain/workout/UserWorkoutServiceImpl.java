package pm.workout.helper.domain.workout;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pm.workout.helper.api.workout.request.SaveUserWorkoutRequest;
import pm.workout.helper.domain.workout.doc.SeriesRepetitionsDetails;
import pm.workout.helper.domain.workout.doc.UserWorkout;
import pm.workout.helper.domain.workout.doc.Workout;
import pm.workout.helper.domain.workout.doc.WorkoutAssessment;
import pm.workout.helper.domain.workout.doc.WorkoutPart;
import pm.workout.helper.domain.workout.dto.DashboardStatisticsDto;
import pm.workout.helper.domain.workout.dto.UserWorkoutDto;
import pm.workout.helper.domain.workout.dto.WorkoutsPerPlanDto;
import pm.workout.helper.domain.workout.dto.basic_statistics.VolumeExerciseDetailsDto;
import pm.workout.helper.domain.workout.dto.basic_statistics.VolumeExerciseDto;
import pm.workout.helper.domain.workout.dto.number_statistics.ExerciseSeriesDetailsDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserWorkoutServiceImpl implements UserWorkoutService {
    private final UserWorkoutRepository userWorkoutRepository;
    private final UserWorkoutMapper userWorkoutMapper;

    @Override
    public void saveUserWorkoutSession(String userId, SaveUserWorkoutRequest request) {
        String workoutId = UUID.randomUUID().toString().replaceAll("-", "");
        request.getWorkoutParts().forEach(part -> System.out.println(part.toString()));
        Workout workout = new Workout(workoutId, Long.parseLong(request.getTrainingPlanId()), Long.parseLong(request.getTrainingUnitId()),
                request.getStartedAt(), request.getFinishedAt(), request.getTrainingDay(), request.getWorkoutParts().stream()
                .map(rPart -> new WorkoutPart(rPart.getTrainingUnitPartId(), rPart.getExerciseId(), rPart.getExerciseName(),
                        rPart.getSeriesRepetitionsDetails().stream()
                                .map(ssPart -> new SeriesRepetitionsDetails(ssPart.getSeriesNumber(),
                                        ssPart.getPerformedRepetitionsNumber(), ssPart.getTargetSeriesRepetitionsNumber(), ssPart.getUsedWeight()))
                                .toList()))
                .toList(), mapRequestedWorkoutAssessment(request.getWorkoutAssessment()));
        userWorkoutRepository.saveUserWorkout(userId, workout);
    }

    private WorkoutAssessment mapRequestedWorkoutAssessment(WorkoutAssessment workoutAssessment){
        return new WorkoutAssessment(Optional.ofNullable(workoutAssessment.getAdditionalComment()).orElse(""),
                Optional.ofNullable(workoutAssessment.getPersonalRate()).orElse(5));
    }

    @Override
    public List<UserWorkoutDto> getAllUserWorkouts(String userId) {
        Optional<UserWorkout> userWorkout = userWorkoutRepository.getUserWorkouts(userId);
        if (userWorkout.isPresent()){
            System.out.println(userWorkout.get().getPerformedWorkouts());
            return userWorkout.get().getPerformedWorkouts()
                    .stream()
                    .map(userWorkoutMapper::toDto)
                    .sorted((o1, o2) -> o2.getFinishedAt().compareTo(o1.getFinishedAt()))
                    .toList();
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public UserWorkoutDto getUserWorkout(String workoutId) {
        return userWorkoutMapper.toDto(
                userWorkoutRepository
                        .getWorkout(workoutId)
                        .orElseThrow(() -> new NotFoundException("Workout not found")));
    }

    @Override
    public UserWorkoutDto getLatestUserWorkout(String userId, String trainingUnitId) {
        Optional<UserWorkout> userWorkout = userWorkoutRepository.getUserWorkouts(userId);
        return userWorkout.map(value -> value.getPerformedWorkouts()
                .stream()
                .filter(workout -> workout.getTrainingUnitId() == Long.parseLong(trainingUnitId))
                .min((w1, w2) -> w2.getFinishedAt().compareTo(w1.getFinishedAt()))
                .map(userWorkoutMapper::toDto)
                .orElseGet(UserWorkoutDto::createEmptyDto))
                .orElseGet(UserWorkoutDto::createEmptyDto);
    }

    @Override
    public List<UserWorkoutDto> getAllUserWorkoutsForTrainingUnit(String userId, String trainingUnitId) {
        return userWorkoutRepository.getUserWorkouts(userId)
                .map(value -> value.getPerformedWorkouts()
                        .stream()
                        .filter(workout -> workout.getTrainingUnitId() == Long.parseLong(trainingUnitId))
                        .map(userWorkoutMapper::toDto)
                        .toList())
                .orElseGet(Collections::emptyList);
    }

    @Override
    public List<UserWorkoutDto> getAllUserWorkoutsForTrainingPlan(String userId, String trainingPlanId) {
        return userWorkoutRepository.getUserWorkouts(userId)
                .map(value -> value.getPerformedWorkouts()
                        .stream()
                        .filter(workout -> workout.getTrainingPlanId() == Long.parseLong(trainingPlanId))
                        .map(userWorkoutMapper::toDto)
                        .toList())
                .orElseGet(Collections::emptyList);
    }

    @Override
    public void deleteUserWorkout(String workoutId) {
        UserWorkout userWorkout = userWorkoutRepository
                .findUserWorkoutsByWorkoutId(workoutId)
                .orElseThrow(() -> new NotFoundException("Workout not found"));

        userWorkout.getPerformedWorkouts()
                .removeIf(workout -> workout.getWorkoutId().equals(workoutId));

        userWorkoutRepository.saveWorkout(userWorkout);
    }

    @Override
    public List<VolumeExerciseDto> getVolumeOneRepMaxStatistics(String userId, String trainingPlanId) {
        List<Workout> workouts = userWorkoutRepository.getUserWorkouts(userId)
                .map(value -> value.getPerformedWorkouts()
                        .stream()
                        .filter(workout -> workout.getTrainingPlanId() == Long.parseLong(trainingPlanId))
                        .toList()).orElse(Collections.emptyList());
        if (workouts.isEmpty()) {
            return Collections.emptyList();
        } else {
            return splitWorkoutsByTrainingUnit(workouts);
        }
    }

    private List<VolumeExerciseDto> splitWorkoutsByTrainingUnit(List<Workout> performedWorkouts){
        return performedWorkouts.stream()
                .map(workout ->
                        new VolumeExerciseDto(workout.getWorkoutId(), workout.getFinishedAt(), workout.getTrainingUnitId(),
                                workout.getTrainingDay(),
                                workout.getWorkoutParts()
                                        .stream()
                                        .map(part ->
                                                new VolumeExerciseDetailsDto(part.getExerciseId(),
                                                        part.getExerciseName(), part.calculateVolume(),
                                                        part.calculateOneRepMax())).toList()))
                .toList();
    }

    @Override
    public List<ExerciseSeriesDetailsDto> getExerciseSeriesDetails(String userId, String trainingPlanId) {
        List<Workout> workouts = userWorkoutRepository.getUserWorkouts(userId)
                .map(value -> value.getPerformedWorkouts()
                        .stream()
                        .filter(workout -> workout.getTrainingPlanId() == Long.parseLong(trainingPlanId))
                        .toList()).orElse(Collections.emptyList());
        if (workouts.isEmpty()) {
            return Collections.emptyList();
        } else {
            return splitWorkoutsByExercise(workouts);
        }
    }

    private List<ExerciseSeriesDetailsDto> splitWorkoutsByExercise(List<Workout> workouts){
       return workouts.stream().map(workout -> workout.getWorkoutParts().stream()
                .map(part -> new ExerciseSeriesDetailsDto(part.getExerciseId(), part.getExerciseName(),
                        part.calculateTotalSeries(), part.calculateTotalRepetitions())).toList())
                .flatMap(List::stream)
                .collect(Collectors.toMap(ExerciseSeriesDetailsDto::getExerciseId, Function.identity(), (o1, o2) ->
                        new ExerciseSeriesDetailsDto(o1.getExerciseId(), o1.getExerciseName(),
                                o1.getTotalSeries() + o2.getTotalSeries(),
                                o1.getTotalReps() + o2.getTotalReps())))
                .values().stream().toList();

    }

    @Override
    public DashboardStatisticsDto getDashboardStatistics(String userId) {
        UserWorkout userWorkouts = userWorkoutRepository.getUserWorkouts(userId).orElseThrow(() -> new NotFoundException("User not found"));
        int totalWorkouts = userWorkouts.getPerformedWorkouts().size();
        int totalTrainingUnits = userWorkouts.getPerformedWorkouts().stream()
                .map(Workout::getWorkoutId)
                .distinct()
                .toList()
                .size();
        long totalTrainingMinutes = userWorkouts.getPerformedWorkouts()
                .stream().map(Workout::getWorkoutTime)
                .reduce(0L, Long::sum);
        long averageTrainingTime = (totalTrainingMinutes/totalWorkouts);
        double averageTrainingRate = (double) userWorkouts.getPerformedWorkouts()
                .stream().map(w -> w.getWorkoutAssessment().getPersonalRate())
                .reduce(0, Integer::sum)/totalWorkouts;

        List<WorkoutsPerPlanDto> workoutsPerPlanDtos = userWorkouts.getPerformedWorkouts()
                .stream()
                .collect(Collectors.groupingBy(Workout::getTrainingPlanId))
                .entrySet()
                .stream()
                .map(entry -> new WorkoutsPerPlanDto(entry.getKey(), entry.getValue().size()))
                .toList();

        return new DashboardStatisticsDto(
                totalWorkouts, totalTrainingUnits,
                totalTrainingMinutes, averageTrainingTime,
                averageTrainingRate, workoutsPerPlanDtos);
    }
}

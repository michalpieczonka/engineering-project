package pm.workout.helper.domain.training.plan;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pm.workout.helper.api.training.plan.request.CreateTrainingPlanRequest;
import pm.workout.helper.api.training.plan.request.CreateTrainingUnitPartRequest;
import pm.workout.helper.api.training.plan.request.UpdateTrainingPlanRequest;
import pm.workout.helper.api.training.plan.request.UpdateTrainingUnitPartRequest;
import pm.workout.helper.api.training.plan.request.plan.AddTrainingDayRequest;
import pm.workout.helper.api.training.plan.request.plan.AddTrainingPlanRateRequest;
import pm.workout.helper.domain.training.exception.TrainingPlanNotFoundException;
import pm.workout.helper.domain.training.plan.dto.PublicTrainingPlanDto;
import pm.workout.helper.domain.training.plan.dto.TrainingPlanDetailsDto;
import pm.workout.helper.domain.training.plan.dto.TrainingPlanDto;
import pm.workout.helper.domain.training.plan.dto.template.TrainingUnitTemplateDto;
import pm.workout.helper.domain.training.plan.rate.TrainingPlanRate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
class TrainingPlanServiceImpl implements TrainingPlanService{
    private final TrainingPlanRepository trainingPlanRepository;
    private final TrainingPlanMapper trainingPlanMapper;
    private final TrainingPlanFactory trainingPlanFactory;

    @Transactional(readOnly = true)
    @Override
    public List<TrainingPlanDto> getAllUserTrainingPlans(Long userId) {
        return trainingPlanRepository.getAllUserTrainingPlans(userId)
                .stream().map(trainingPlanMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public TrainingPlanDetailsDto getTrainingPlanDetails(Long trainingPlanId) {
        TrainingPlan trainingPlan = trainingPlanRepository.getTrainingPlanDetails(trainingPlanId)
                .orElseThrow(() -> new TrainingPlanNotFoundException(trainingPlanId));
        return trainingPlanMapper.toDetailsDto(trainingPlan);
    }

    @Transactional
    @Override
    public Long createNewTrainingPlan(CreateTrainingPlanRequest request, Long userId) {
        TrainingPlan trainingPlan = trainingPlanFactory.buildAndValidateTrainingPlan(request, userId);
        return trainingPlanRepository.createNewTrainingPlan(trainingPlan).getId();
    }


    @Transactional
    @Override
    public void deleteTrainingPlan(Long trainingPlanId) {
		//todo: implement
    }

    @Transactional(readOnly = true)
    @Override
    public TrainingUnitTemplateDto getTrainingUnitPartTemplate(Long trainingPlanId, TrainingDay trainingDay) {
        TrainingUnit trainingUnit = trainingPlanRepository.getTrainingUnit(trainingPlanId, trainingDay).orElseThrow(() -> new TrainingPlanNotFoundException(trainingPlanId));
        return trainingPlanMapper.mapToTemplate(trainingPlanId, trainingUnit);
    }

    @Transactional
    @Override
    public void updateTrainingPlan(Long trainingPlanId, UpdateTrainingPlanRequest updateTrainingPlanRequest) {
        TrainingPlan trainingPlan = trainingPlanRepository.getTrainingPlanById(trainingPlanId)
                .orElseThrow(() -> new TrainingPlanNotFoundException(trainingPlanId));

        TrainingPlan updatedPlan = trainingPlan.toBuilder()
                .title(updateTrainingPlanRequest.getTitle())
                .description(updateTrainingPlanRequest.getPlanDescription())
                .numberOfTrainingDays(updateTrainingPlanRequest.getTrainingDays().size())
                .planPriority(updateTrainingPlanRequest.getPlanPriority())
                .type(updateTrainingPlanRequest.getPlanType())
                .isPublic(updateTrainingPlanRequest.isPublic())
                .latestModificationDate(LocalDateTime.now())
                .build();

        updatedPlan.getTrainingUnits().forEach(unit -> {
            if(!updateTrainingPlanRequest.getTrainingDays().contains(unit.getTrainingDay())){
                trainingPlan.getTrainingUnits().remove(unit);
            }
        });

        updateTrainingPlanRequest.getTrainingDays().forEach(trainingDay -> {
            Optional<TrainingUnit> trainingUnit = trainingPlan.getTrainingUnits().stream()
                    .filter(unit -> unit.getTrainingDay().equals(trainingDay))
                    .findFirst();
            if(trainingUnit.isEmpty()){
                TrainingUnit newTrainingUnit = TrainingUnit.builder()
                        .trainingDay(trainingDay)
                        .trainingPlan(trainingPlan)
                        .build();
                trainingPlan.addTrainingUnit(newTrainingUnit);
            }
        });

        trainingPlanRepository.updateTrainingPlan(updatedPlan);
    }

    @Transactional
    @Override
    public void deleteTrainingUnitPart(Long trainingPlanId, Long trainingUnitId, Long trainingUnitPartId) {
        TrainingPlan trainingPlan = trainingPlanRepository.getTrainingPlanById(trainingPlanId)
                .orElseThrow(() -> new TrainingPlanNotFoundException(trainingPlanId));

        trainingPlan.getTrainingUnits()
                .stream()
                .filter(trainingUnit -> trainingUnit.getId().equals(trainingUnitId))
                .findFirst()
                .ifPresent(trainingUnit -> trainingUnit.getUnitParts()
                        .removeIf(trainingUnitPart -> trainingUnitPart.getId().equals(trainingUnitPartId)));
        trainingPlan.setLatestModificationDate(LocalDateTime.now());
    }


    @Transactional
    @Override
    public void addTrainingUnitPart(Long trainingPlanId, Long trainingUnitId, CreateTrainingUnitPartRequest request) {
        TrainingPlan trainingPlan = trainingPlanRepository.getTrainingPlanById(trainingPlanId)
                .orElseThrow(() -> new TrainingPlanNotFoundException(trainingPlanId));

        TrainingUnit trainingUnit = trainingPlan.getTrainingUnits()
                .stream()
                .filter(unit -> unit.getId().equals(trainingUnitId))
                .findFirst()
                .orElseThrow(() -> new TrainingPlanNotFoundException(trainingPlanId));

        trainingPlanFactory.buildTrainingUnitPart(trainingUnit, request);
        trainingPlan.setLatestModificationDate(LocalDateTime.now());
        trainingPlanRepository.updateTrainingPlan(trainingPlan);
    }

    @Transactional
    @Override
    public void updateTrainingUnitPart(Long trainingPlanId, Long trainingUnitId, Long trainingUnitPartId, UpdateTrainingUnitPartRequest request) {
        request.getSeriesRepetitionsDetails().forEach(d -> System.out.println(d.getSeriesNumber() + " powtorzen: "+d.getRepetitionsNumber() ));
        TrainingPlan trainingPlan = trainingPlanRepository.getTrainingPlanById(trainingPlanId)
                .orElseThrow(() -> new TrainingPlanNotFoundException(trainingPlanId));

        TrainingUnitPart trainingUnitPart = trainingPlan.getTrainingUnits()
                .stream()
                .filter(unit -> unit.getId().equals(trainingUnitId))
                .findFirst()
                .orElseThrow(() -> new TrainingPlanNotFoundException(trainingPlanId))
                .getUnitParts().stream().filter(part -> part.getId().equals(trainingUnitPartId))
                .findFirst()
                .orElseThrow(() -> new TrainingPlanNotFoundException(trainingPlanId));

        request.getSeriesRepetitionsDetails()
                .forEach(seriesRepetitionsDetail ->
                        trainingUnitPart.getSeriesRepetitionsMap()
                                .put(seriesRepetitionsDetail.getSeriesNumber(), seriesRepetitionsDetail.getRepetitionsNumber()));

        trainingUnitPart.getSeriesRepetitionsMap().keySet().forEach(key -> {
            if(request.getSeriesRepetitionsDetails().stream().noneMatch(d -> d.getSeriesNumber() == (key))){
                trainingUnitPart.getSeriesRepetitionsMap().remove(key);
            }
        });

        trainingPlan.setLatestModificationDate(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    @Override
    public List<PublicTrainingPlanDto> getAllPublicTrainingPlans(Long requestedByUserId) {
        return trainingPlanRepository.getAllPublicTrainingPlans()
                .stream()
                .filter(TrainingPlan::isPublic)
                .map(plan -> trainingPlanMapper.toPublicDto(plan, requestedByUserId))
                .toList();
    }

    @Transactional
    @Override
    public void copyTrainingPlan(Long trainingPlanId, Long userId) {
       TrainingPlan trainingPlan = trainingPlanRepository.getTrainingPlanById(trainingPlanId).orElseThrow(() -> new TrainingPlanNotFoundException(trainingPlanId));
       trainingPlan.addPlanUser(userId);

       TrainingPlan copiedPlan = TrainingPlan.builder()
               .title(trainingPlan.getTitle()+" - kopia")
               .description(trainingPlan.getDescription())
               .numberOfTrainingDays(trainingPlan.getNumberOfTrainingDays())
               .planPriority(trainingPlan.getPlanPriority())
               .isPublic(trainingPlan.isPublic())
               .planLength(trainingPlan.getPlanLength())
               .type(trainingPlan.getType())
               .preferredTrainingInternship(trainingPlan.getPreferredTrainingInternship())
               .creationDate(LocalDateTime.now())
               .latestModificationDate(LocalDateTime.now())
               .targetFinishDate(calculateNewPlanTargetFinishTime(trainingPlan.getCreationDate().toLocalDate(), trainingPlan.getTargetFinishDate()))
               .trainingUnits(new HashSet<>())
               .planUsersIds(new HashSet<>())
               .build();

       copiedPlan.setPlanOwner(userId);
       trainingPlanRepository.createNewTrainingPlanCopy(copiedPlan, trainingPlan.getTrainingUnits());
    }

    private LocalDate calculateNewPlanTargetFinishTime(LocalDate creationDate, LocalDate baseFinishTime){
        return LocalDate.now().plusDays(ChronoUnit.DAYS.between(creationDate, baseFinishTime));
    }

    @Transactional
    @Override
    public void addTrainingPlanRate(Long trainingPlanId, AddTrainingPlanRateRequest request) {
        TrainingPlan trainingPlan = trainingPlanRepository.getTrainingPlanById(trainingPlanId).orElseThrow(() -> new TrainingPlanNotFoundException(trainingPlanId));
        TrainingPlanRate rate = TrainingPlanRate.builder()
                .rate(request.getRateValue())
                .description(request.getCommentText())
                .rateAuthorUserId(request.getRateAuthorId())
                .rateDate(LocalDateTime.now())
                .build();
        trainingPlan.addPlanRate(rate);
    }
}

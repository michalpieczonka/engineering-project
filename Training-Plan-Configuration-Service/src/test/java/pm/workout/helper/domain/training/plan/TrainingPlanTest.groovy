package pm.workout.helper.domain.training.plan

import pm.workout.helper.domain.training.plan.rate.TrainingPlanRate
import spock.lang.Specification

class TrainingPlanTest extends Specification {
    def "when trying to get plan rate should return valid value"() {
        given:
        def trainingPlan = buildTrainingPlan(1L, " ", false, Set.of(), Set.of(),
                Set.of(buildRate(1L, "ez", 9), buildRate(2L, "hard", 3)))

        when:
        def rate = trainingPlan.getPlanRate()

        then:
        rate == 6
    }

    private TrainingPlan buildTrainingPlan(Long id, String title, boolean isPublic, Set<Long> planUsersIds = Set.of(), Set<TrainingUnit> trainingUnits = Set.of(), Set<TrainingPlanRate> planRates = Set.of()) {
        return TrainingPlan
                .builder()
        .id(id)
        .title(title)
        .description("desc")
        .planPriority(PlanPriority.CHEST)
        .type(TrainingPlanType.FULL_BODY_WORKOUT)
        .isPublic(true)
        .planUsersIds(planUsersIds)
        .planRates(planRates)
        .trainingUnits(trainingUnits)
                .build()
    }

    private TrainingPlanRate buildRate(Long id, String comment, Integer value){
        return TrainingPlanRate
                .builder()
                .id(id)
                 .description(comment)
                .rate(value)
                .build();
    }
}

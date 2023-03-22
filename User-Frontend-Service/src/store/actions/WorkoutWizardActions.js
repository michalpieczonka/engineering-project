
export const NAME_ASSIGNED = 'NAME_ASSIGNED';
export const TRAINING_DAY_ADDED = 'TRAINING_DAY_ADDED';
export const PLAN_PRIORITY_ASSIGNED = 'PLAN_PRIORITY_ASSIGNED';
export const PLAN_PUBLICITY_SPECIFIED = 'PLAN_PUBLICITY_SPECIFIED';
export const ADDITIONAL_INFORMATIONS_ASSIGNED = 'ADDITIONAL_INFORMATIONS_ASSIGNED';
export const EXERCISE_ADDED = 'EXERCISE_ADDED';
export const EXERCISE_DELETED = "EXERCISE_DELETED"
export const TRAINING_PLAN_SAVED = "TRAINING_PLAN_SAVED"
export const PLAN_TYPE_SPECIFIED = "PLAN_TYPE_SPECIFIED"
export const PLAN_FINISH_DATE_ASSIGNED = "PLAN_FINISH_DATE_ASSIGNED"

export function assignName(planName){
    return {
        type: NAME_ASSIGNED,
        payload: planName,
    };
}

export function addTrainingDay (trainingDay) {
    return {
        type: TRAINING_DAY_ADDED,
        payload: trainingDay
    };
}

export function assignPlanPriority (planPriority) {
    return{
        type: PLAN_PRIORITY_ASSIGNED,
        payload: planPriority
    };
}

export function specifyPlanPublicity (planPublicity) {
    return {
        type: PLAN_PUBLICITY_SPECIFIED,
        payload: planPublicity
    };
}

export function specifyPlanFinishDate (date){
    return {
        type: PLAN_FINISH_DATE_ASSIGNED,
        payload: date
    }
}

export function specifyAdditionalInformations (additionalInformations){
    return {
        type: ADDITIONAL_INFORMATIONS_ASSIGNED,
        payload: additionalInformations
    };
}

export function specifyPlanType (planType){
    return {
        type: PLAN_TYPE_SPECIFIED,
        payload: planType
    };
}

export function addExercise(exercise){
    return {
        type: EXERCISE_ADDED,
        payload: exercise
    };
}

export function removeExercise(exercise){
    return {
        type: EXERCISE_DELETED,
        payload: exercise
    };
}

export function clearTrainingPlan(){
    return {
        type: TRAINING_PLAN_SAVED,
        payload: void 0
    };
}

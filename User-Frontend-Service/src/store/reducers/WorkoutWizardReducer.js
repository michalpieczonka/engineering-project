import {
    NAME_ASSIGNED,
    TRAINING_DAY_ADDED,
    PLAN_PRIORITY_ASSIGNED,
    PLAN_PUBLICITY_SPECIFIED,
    ADDITIONAL_INFORMATIONS_ASSIGNED,
    EXERCISE_ADDED,
    EXERCISE_DELETED,
    TRAINING_PLAN_SAVED,
    PLAN_TYPE_SPECIFIED,
    PLAN_FINISH_DATE_ASSIGNED
} from '../actions/WorkoutWizardActions';

const initialState = {
    planName: '',
    trainingDays: [],
    planPriorirty: "UNSPECIFIED",
    planType: "HYBRID",
    isPublic: false,
    additionalInformations: '',
    targetFinishDate: '',
    exercises: []
};

export default function workoutWizardReducer(state = initialState, action) {
    if (action.type  === NAME_ASSIGNED ){
        return {
            ...state,
            planName: action.payload
        };
    } 
    if (action.type === TRAINING_DAY_ADDED){
        return {
            ...state,
            trainingDays: Array.isArray(action.payload) ? action.payload.map(x => x.value) : []
        };
    }

    if (action.type === PLAN_PRIORITY_ASSIGNED){
        return {
            ...state,
            planPriorirty: action.payload
        };
    }

    if(action.type === PLAN_PUBLICITY_SPECIFIED){
        return {
            ...state,
            isPublic: action.payload
        };
    }
    if (action.type === ADDITIONAL_INFORMATIONS_ASSIGNED){
        return {
            ...state,
            additionalInformations: action.payload
        };
    }

    if (action.type === EXERCISE_ADDED){
        return {
            ...state,
            exercises: [...state.exercises, action.payload]
        }
    }

    if (action.type === EXERCISE_DELETED){
        const newExercises = state.exercises.filter((x) =>  JSON.stringify(x) !== JSON.stringify(action.payload) );

        return {
            ...state,
            exercises: newExercises
        }
    }

    if (action.type === TRAINING_PLAN_SAVED){
        return {
            state: initialState
        }
    }

    if (action.type === PLAN_TYPE_SPECIFIED){
        return {
            ...state,
            planType: action.payload
        }
    }

    if (action.type === PLAN_FINISH_DATE_ASSIGNED){
        return {
            ...state,
            targetFinishDate: action.payload
        }
    }

    return state;
}

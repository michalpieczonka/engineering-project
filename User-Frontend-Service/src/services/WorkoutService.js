import axiosInstance from '../services/AxiosInstance';

export function getWorkoutTemplate(userId, trainingPlanId,  trainingUnitId){
    const url = `/users/${userId}/training-plans/${trainingPlanId}/training-units/${trainingUnitId}/template`
    return axiosInstance.get(url);
}

export function saveWorkout(userId, workoutSession){
    const url = `/users/${userId}/workouts`
    return axiosInstance.post(url, workoutSession);
}

export function getAllUserWorkouts(userId){
    const url = `/users/${userId}/workouts`
    return axiosInstance.get(url);
}

export function getWorkout(workoutId){
    const url = `/workouts/${workoutId}`
    return axiosInstance.get(url);
}

export function getLatestUserWorkoutForTrainingUnit(userId, trainingUnitId){
    const url = `/workouts/${userId}/workouts/training-units/${trainingUnitId}/latest`
    return axiosInstance.get(url);
}

export function getWorkoutVolumeOneRepStatistics(userId, trainingPlanId){
    const url = `/workouts/${userId}/training-plans/${trainingPlanId}/statistics`
    return axiosInstance.get(url);
}

export function getWorkoutTotalStatistics(userId, trainingPlanId){
    const url = `/workouts/${userId}/training-plans/${trainingPlanId}/total-statistics`
    return axiosInstance.get(url);
}
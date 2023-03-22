import axiosInstance from '../services/AxiosInstance';

export function getAllExercises() {
    return axiosInstance.get(`/exercises/all`);
}

export function getAllExercisesByTargetMuscle(muscleGroup){
    return axiosInstance.get('/exercises?targetMuscle='+muscleGroup.replace('-', '_').toUpperCase());
}

export function addNewExercise(exercise){
    return axiosInstance.post('/exercises', exercise);
}

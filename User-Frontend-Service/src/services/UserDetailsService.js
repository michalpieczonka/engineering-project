import { store } from '../store/store';
import axiosInstance from '../services/AxiosInstance';

export function getUserProfileDetails(){
    const state = store.getState();
    let userId = state.auth.auth.userId;
    return axiosInstance.get('users/'+userId);
}

export function getUserExtendedProfileDetails(userId){
    return axiosInstance.get( `users/${userId}/details`);
}

export function updateUserHealthDetails(userId, healthDetails){
    return axiosInstance.put(`users/${userId}/health-details`, healthDetails);
}

export function addUserphoto(userId, photo){
    return axiosInstance.post(`users/${userId}/photos`, photo, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
    });
}

export function getUserPhoto(userPhotoId){
    return axiosInstance.get(`users/photos/${userPhotoId}/details`);
}

export function getUserPhotoDetails(userPhotoId){
    return axiosInstance.get(`users/photos/${userPhotoId}/details`);
}

export function changeUserPassword(userId, passwords){
    return axiosInstance.put(`users/${userId}/password`, passwords);
}

export function updateUserDetails(userId, userDetails){
    return axiosInstance.put(`users/${userId}`, userDetails);
}

export function deleteUserPhoto(userPhotoId){
    return axiosInstance.delete(`users/photos/${userPhotoId}`);
}

export function getUserWorkoutStatistics(userId){
    return axiosInstance.get(`users/${userId}/statistics`);
}
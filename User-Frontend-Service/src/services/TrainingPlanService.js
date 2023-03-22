import axiosInstance from '../services/AxiosInstance';
import { store } from '../store/store';
import React, { useState } from 'react'

const state = store.getState();


export function addNewTrainingPlan(trainingPlan, userId){
    return axiosInstance.post(`/training-plans/${userId}`, trainingPlan);
}

export function getAllUserTrainingPlans(userId){
    const url = `/training-plans/${userId}/all`
    return axiosInstance.get(url)
}

export function getTrainingPlan(trainingPlanId){
    const url = `/training-plans/${trainingPlanId}`
    return axiosInstance.get(url)
}

export function updateTrainingPlanDetails(trainingPlanId, trainingPlanDetails){
    const url = `/training-plans/${trainingPlanId}`
    return axiosInstance.put(url, trainingPlanDetails)
}

export function deleteTrainingUnitPart(trainingPlanId, trainingUnitId, trainingUnitPartId){
    const url = `/training-plans/${trainingPlanId}/training-units/${trainingUnitId}/unit-parts/${trainingUnitPartId}`
    return axiosInstance.delete(url)
}

export function addTrainingUnitPart(trainingPlanId, trainingUnitId, createTrainingUnitPartRequest){
    const url = `/training-plans/${trainingPlanId}/training-units/${trainingUnitId}`
    return axiosInstance.post(url, createTrainingUnitPartRequest)
}

export function updateTrainingUnitPart(trainingPlanId,  trainingUnitId, trainingUnitPartId, updateTrainingUnitPartRequest){
    const url = `/training-plans/${trainingPlanId}/training-units/${trainingUnitId}/unit-parts/${trainingUnitPartId}`
    return axiosInstance.put(url, updateTrainingUnitPartRequest)
}

export function getCurrentTrainingPlan(userId){
    const url = `/users/${userId}/training-plan`
    return axiosInstance.get(url)
}

export function setCurrentUserTrianingPlan(userId, trainingPlanId){
    const url = `/users/${userId}/training-plan/${trainingPlanId}`
    return axiosInstance.put(url)
}

export function getAllPublicTrainingPlans(userId){
    const url = `/training-plans/public?requestedByUserId=${userId}`
    return axiosInstance.get(url)
}

export function copyTrainingPlan(trainingPlanId, forUserId){
    const url = `/training-plans/${trainingPlanId}/copy/${forUserId}`
    return axiosInstance.post(url)
}

export function addTrainingPlanRate(trainingPlanId, request){
    const url = `/training-plans/${trainingPlanId}/rates`
    return axiosInstance.post(url, request)
}
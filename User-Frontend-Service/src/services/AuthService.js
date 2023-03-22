import axios from 'axios';
import swal from "sweetalert";
import {loginConfirmedAction, logout,} from '../store/actions/AuthActions';
import User from "../models/user";
import {BASE_API_URL} from "../context/Constants";

const BASE_URL = BASE_API_URL + '/api/authentication'

export function signUp(username, email, password, gender, dateOfBirth, trainSince) {
    let request = {
        username: username,
        password: password,
        email: email,
        dateOfBirth: dateOfBirth,
        trainingStartDate: trainSince,
        gender: gender
    }
    
    
    return axios.post(
        BASE_URL+ '/register',
        request,
    );
}

export function login(email, password) {
    return axios.post(
        BASE_URL + '/login',
        new User(email, password),
    );
}

export function formatError(errorResponse) {
    switch (errorResponse.errorCode) {
        case "USER_NOT_EXISTS_ERROR":
            swal("Oops", "Wprowadzone dane logowania są niepoprawne lub użytkownik nie istnieje", "error");
            break;
        case "USER_EXISTS_ERROR":
            swal("Oops",  "Wprowadzona nazwa użytkownika lub/i adres email są już w użyciu", "error")
            break;
        default:
            return swal("Oops", "Wystąpił nieznany błąd", "error");
    }
}

export function saveTokenInLocalStorage(tokenDetails) {
    tokenDetails.expireDate = new Date(
        new Date().getTime() + tokenDetails.expiresIn * 1000,
    );
    localStorage.setItem('userDetails', JSON.stringify(tokenDetails));
}

export function runLogoutTimer(dispatch, timer, history) {
    setTimeout(() => {
        dispatch(logout(history));
    }, timer);
}

export function checkAutoLogin(dispatch, history) {
    const tokenDetailsString = localStorage.getItem('userDetails');
    let tokenDetails = '';
    if (!tokenDetailsString) {
        dispatch(logout(history));
        return;
    }

    tokenDetails = JSON.parse(tokenDetailsString);
    let expireDate = new Date(tokenDetails.expireDate);
    let todaysDate = new Date();

    if (todaysDate > expireDate) {
        dispatch(logout(history));
        return;
    }
    dispatch(loginConfirmedAction(tokenDetails));

    const timer = expireDate.getTime() - todaysDate.getTime();
    runLogoutTimer(dispatch, timer, history);
}

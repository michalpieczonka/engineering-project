import {
    formatError,
    login,
    runLogoutTimer,
    saveTokenInLocalStorage,
    signUp,
} from '../../services/AuthService';
import swal from 'sweetalert'

export const SIGNUP_CONFIRMED_ACTION = '[signup action] confirmed signup';
export const SIGNUP_FAILED_ACTION = '[signup action] failed signup';
export const LOGIN_CONFIRMED_ACTION = '[login action] confirmed login';
export const LOGIN_FAILED_ACTION = '[login action] failed login';
export const LOADING_TOGGLE_ACTION = '[Loading action] toggle loading';
export const LOGOUT_ACTION = '[Logout action] logout action';

export function signupAction(username, email, password, gender, dateOfBirth, trainSince, history) {
    return (dispatch) => {
        signUp(username, email, password, gender, dateOfBirth, trainSince)
        .then((response) => {
            dispatch(confirmedSignupAction(response.data));
            history.push('/login');
        })
        .catch((error) => {
            if(error.response){
                const errorMessage = formatError(error.response.data);
                dispatch(signupFailedAction(errorMessage));
            }else {
                swal("Oops",  "Wystąpił błąd, spróbuj ponownie później", "error");
            }
           
        });
    };
}

export function logout(history) {
    localStorage.removeItem('userDetails');
    history.push('/login');
    return {
        type: LOGOUT_ACTION,
    };
}

export function loginAction(username, password, history) {
    return (dispatch) => {
        login(username, password)
            .then((response) => {
                saveTokenInLocalStorage(response.data);
                runLogoutTimer(
                    dispatch,
                    response.data.expiresIn * 1000,
                    history,
                );
                dispatch(loginConfirmedAction(response.data));
				history.push('/dashboard');
    			window.location.reload();

            })
            .catch((error) => {
                if(error.response){
                    const errorMessage = formatError(error.response.data);
                    dispatch(loginFailedAction(errorMessage));
                } else {
                    swal("Oops",  "Wystąpił błąd, spróbuj ponownie później", "error");
                }
               
            });
    };
}

export function loginFailedAction(data) {
    return {
        type: LOGIN_FAILED_ACTION,
        payload: data,
    };
}

export function loginConfirmedAction(data) {
    return {
        type: LOGIN_CONFIRMED_ACTION,
        payload: data,
    };
}

export function confirmedSignupAction(payload) {
    return {
        type: SIGNUP_CONFIRMED_ACTION,
        payload,
    };
}

export function signupFailedAction(message) {
    return {
        type: SIGNUP_FAILED_ACTION,
        payload: message,
    };
}

export function loadingToggleAction(status) {
    return {
        type: LOADING_TOGGLE_ACTION,
        payload: status,
    };
}

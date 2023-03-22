import axios from 'axios';
import { BASE_API_URL } from '../context/Constants';
import { store } from '../store/store';

const axiosInstance = axios.create({
    baseURL: BASE_API_URL+'/api',
});

axiosInstance.interceptors.request.use((config) => {
    const state = store.getState();
    const token = state.auth.auth.idToken;
    config.params = config.params || {};
    config.headers['Authorization'] = 'Bearer ' + token;
	console.log(config);
    return config;
});

export default axiosInstance;

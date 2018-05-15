import axios from "axios";

const host = process.env.NODE_ENV === 'production' ? 'http://workouts-api.weizilla.com' : 'http://localhost:8080';

export const api = {
    getBuildInfo(cb) {
        return axios.get(host + "/api/build")
            .then(response => response.data)
            .catch(error => Promise.reject(error));
    },
}

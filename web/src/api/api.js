import axios from "axios";
import moment from "moment";

const host = process.env.NODE_ENV === 'production' ? 'http://workouts-api.weizilla.com' : 'http://localhost:8080';

export const api = {
    getBuildInfo(cb) {
        return axios.get(host + "/api/build")
            .then(response => response.data)
            .catch(error => Promise.reject(error));
    },
    getGoals() {
        let today = moment().format("YYYY-MM-DD");
        return axios.get(host + "/api/goals/?date=" + today + "&numDays=7")
            .then(response => response.data)
            .catch(error => Promise.reject(error));
    },
    getAllGoals() {
        return axios.get(host + "/api/goals/")
            .then(response => response.data)
            .catch(error => Promise.reject(error));
    },
    getTypes() {
        return axios.get(host + "/api/types")
            .then(response => response.data)
            .catch(error => Promise.reject(error));
    },
    addGoal(newGoal) {
        return axios.post(host + "/api/goals/", newGoal)
            .then(response => response.data)
            .catch(error => Promise.reject(error));
    }
}

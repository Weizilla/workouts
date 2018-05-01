import axios from "axios";
import moment from "moment";

const host = process.env.NODE_ENV === 'production' ? 'http://workouts-api.weizilla.com' : 'http://localhost:8080';

export const api = {
    getBuildInfo(cb) {
        return this.getFromApi("build");
    },
    getGoals() {
        const today = moment().format("YYYY-MM-DD");
        const path = "goals/?date=" + today + "&numDays=7";
        return this.getFromApi(path);
    },
    getAllGoals() {
        return this.getFromApi("goals");
    },
    getAllRecords() {
        return this.getFromApi("records");
    },
    getTypes() {
        return this.getFromApi("types");
    },
    getWorkouts() {
        return this.getFromApi("stats");
    },
    getActivites() {
        return this.getFromApi("activities");
    },
    getFromApi(path) {
        return axios.get(host + "/api/" + path)
            .then(response => response.data)
            .catch(error => Promise.reject(error));
    },
    addGoal(newGoal) {
        return axios.post(host + "/api/goals/", newGoal)
            .then(response => response.data)
            .catch(error => Promise.reject(error));
    },
    addRecord(newRecord) {
        return axios.post(host + "/api/records/", newRecord)
            .then(response => response.data)
            .catch(error => Promise.reject(error));
    },
    pollActivities() {
        return this.getFromApi("activities/update");
    },
}

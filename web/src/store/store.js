import Vue from "vue";
import Vuex from "vuex";
import { api } from "../api/api";
import moment from 'moment';
import 'moment-timezone';
import { getField, updateField } from 'vuex-map-fields';


Vue.use(Vuex);

function utcToChicago(utcDateTime) {
    return moment.tz(utcDateTime, "UTC").clone().tz("America/Chicago").format();
}


const state = {
    buildTime: null,
    commitId: null,
    timesOfDay: ["Morning", "Afternoon", "Evening"],
    distanceUnits: { mi: 1609.34, km: 1000, m: 1, yd: 0.9144 },
    newGoal: {
        type: undefined,
        date: undefined,
        timeOfDay: undefined,
        durationHr: undefined,
        durationMin: undefined,
        distanceUnit: undefined,
        distanceValue: undefined,
        notes: undefined,
    },
    newRecord: {
        type: undefined,
        date: moment().format("YYYY-MM-DD"),
        outdoor: undefined,
        rating: undefined,
        durationHr: undefined,
        durationMin: undefined,
        distanceUnit: undefined,
        distanceValue: undefined,
        comment: undefined,
    },
    allGoals: [],
    allRecords: [],
    activities: [],
    goals: new Map(),
    workouts: new Map(),
    types: [],
}

const actions = {
    async populateBuildInfo({ state, commit }) {
        const buildInfo = await api.getBuildInfo();
        commit("setBuildInfo", buildInfo);
    },
    async populateGoals({ state, commit }) {
        const goals = await api.getGoals();
        const byDate = new Map();
        goals.forEach(((goal) => {
            const date = goal.date;
            const collection = byDate.get(date);
            if (!collection) {
                byDate.set(date, [goal]);
            } else {
                collection.push(goal);
            }
        }));
        commit("setGoals", byDate);
    },
    async populateTypes({ state, commit }) {
        const types = await api.getTypes();
        commit("setTypes", types);
    },
    async populateAllGoals({ state, commit }) {
        const goals = await api.getAllGoals();
        commit("setAllGoals", goals);
    },
    async populateAllRecords({ state, commit }) {
        const records = await api.getAllRecords();
        commit("setAllRecords", records);
    },
    async populateWorkouts({ state, commit }) {
        const workouts = await api.getWorkouts();
        const byDate = new Map();
        workouts.forEach((workout) => {
            switch (workout.completion) {
                case "NONE":
                    workout.completionClass = "bg-danger";
                    break;
                case "SOME":
                    workout.completionClass = "bg-warning";
                    break;
                case "ALL":
                    workout.completionClass = "bg-success";
                    break;
                case "GOAL":
                    workout.completionClass = ["text-light", "bg-primary"];
                    break;
            }
            byDate.set(workout.date, workout);
        });
        commit("setWorkouts", byDate);
    },
    async populateActivities({ state, commit }) {
        const activities = await api.getActivites();
        commit("setActivites", activities);
    },
    async addGoal({ state, commit }) {
        let durationHr = parseInt(state.newGoal["durationHr"] || "0");
        let durationMin = parseInt(state.newGoal["durationMin"] || "0");
        let duration = (durationHr * 60 + durationMin) * 60;

        let distanceValue = parseInt(state.newGoal["distanceValue"] || "0");
        let distanceMultiplier = state.distanceUnits[state.newGoal["distanceUnit"]];
        let distance = distanceValue * distanceMultiplier;

        let newGoal = {
            type: state.newGoal["type"],
            date: moment(state.newGoal["date"]).format("YYYY-MM-DD"),
            timeOfDay: state.newGoal["timeOfDay"].toUpperCase(),
            rating: state.newGoal["rating"],
            duration: duration,
            distance: distance,
            notes: state.newGoal["notes"]
        };

        await api.addGoal(newGoal);
    },
    async addRecord({ state, commit }) {
        let durationHr = parseInt(state.newRecord['durationHr'] || "0");
        let durationMin = parseInt(state.newRecord['durationMin'] || "0");
        let duration = (durationHr * 60 + durationMin) * 60;

        let distanceValue = parseInt(state.newRecord["distanceValue"] || "0");
        let distanceMultiplier = state.distanceUnits[state.newRecord["distanceUnit"]];
        let distance = distanceValue * distanceMultiplier;

        let newRecord = {
            type: state.newRecord['type'],
            outdoor: state.newRecord['outdoor'] === 'true',
            date: moment(state.newRecord['date']).format("YYYY-MM-DD"),
            rating: state.newRecord['rating'],
            duration: duration,
            distance: distance,
            comment: state.newRecord['comment']
        };

        await api.addRecord(newRecord);
    },
    async pollActivities({ state, commit }) {
        await api.pollActivities();
    }
}

const getters = {
    allWorkouts: state => {
        const all = []
        state.workouts.forEach((stat) => all.push(...stat.workoutStats));
        return all;
    },
    getField,
}

const mutations = {
    setBuildInfo(state, buildInfo) {
        state.buildTime = utcToChicago(buildInfo["git.build.time"]);
        state.commitId = buildInfo["git.commit.id.abbrev"];
    },
    setGoals(state, goals) {
        state.goals = goals;
    },
    setTypes(state, types) {
        state.types = types;
    },
    setAllGoals(state, goals) {
        state.allGoals = goals;
    },
    setAllRecords(state, records) {
        state.allRecords = records;
    },
    setWorkouts(state, workouts) {
        state.workouts = workouts;
    },
    setActivites(state, activities) {
        state.activities = activities;
    },
    updateField,
}

export const store = new Vuex.Store({
    strict: process.env.NODE_ENV !== 'production',
    state,
    getters,
    actions,
    mutations,
});

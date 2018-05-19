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
    goals: new Map(),
    newGoal: {
        type: "",
        date: moment().format("YYYY-MM-DD"),
        timeOfDay: undefined,
        durationHr: undefined,
        durationMin: undefined,
        distanceUnit: undefined,
        distanceValue: undefined,
        notes: undefined,
    },
    allGoals: [],
    types: [],
}

const actions = {
    async populateBuildInfo({ state, commit }) {
        const buildInfo = await api.getBuildInfo();
        commit("setBuildInfo", buildInfo);
    },
    async populateGoals({ state, commit }) {
        const goals = await api.getGoals();
        console.log(goals);
        const allGoals = new Map();
        goals.forEach(((goal) => {
            const date = goal.date;
            const collection = allGoals.get(date);
            if (!collection) {
                allGoals.set(date, [goal]);
            } else {
                collection.push(goal);
            }
        }));
        commit("setGoals", allGoals);
    },
    async populateTypes({ state, commit }) {
        const types = await api.getTypes();
        commit("setTypes", types);
    },
    async populateAllGoals({ state, commit }) {
        const goals = await api.getAllGoals();
        commit("setAllGoals", goals);
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
    }
}

const getters = {
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
    updateField,
}

export const store = new Vuex.Store({
    strict: process.env.NODE_ENV !== 'production',
    state,
    getters,
    actions,
    mutations,
});

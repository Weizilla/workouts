import Vue from "vue";
import Vuex from "vuex";
import { api } from "../api/api";
import moment from 'moment';
import 'moment-timezone';

Vue.use(Vuex);

function utcToChicago(utcDateTime) {
    return moment.tz(utcDateTime, "UTC").clone().tz("America/Chicago").format();
}

const state = {
    buildTime: null,
    commitId: null,
    goals: new Map(),
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
    }
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
}

export const store = new Vuex.Store({
    strict: process.env.NODE_ENV !== 'production',
    state,
    actions,
    mutations,
});

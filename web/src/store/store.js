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
    commitId: null
}

const actions = {
    async populateBuildInfo({ state, commit }) {
        const buildInfo = await api.getBuildInfo();
        commit("setBuildInfo", buildInfo);
    }
}

const mutations = {
    setBuildInfo(state, buildInfo) {
        state.buildTime = utcToChicago(buildInfo["git.build.time"]);
        state.commitId = buildInfo["git.commit.id.abbrev"];
    }
}

export const store = new Vuex.Store({
    strict: process.env.NODE_ENV !== 'production',
    state,
    actions,
    mutations,
});

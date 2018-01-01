"use strict";

import Vue from 'vue';
import VueRouter from 'vue-router';
import App from './App.vue';
import Records from './Records.vue';
import Goals from './Goals.vue';
import 'moment';
import 'moment-timezone';
import VueResource from 'vue-resource'

function utcToChicago(utcDateTime) {
    return moment.tz(utcDateTime, "UTC").clone().tz("America/Chicago").format();
}

Vue.use(VueResource);
Vue.use(VueRouter);

const routes = [
    { path: '/records', component: Records},
    { path: '/goals', component: Goals},
    { path: '/', component: App},
];

const router = new VueRouter({routes});

new Vue({
    el: "#app",
    router: router
});


"use strict";

import Vue from 'vue';
import VueRouter from 'vue-router';
import App from './App.vue';
import Records from './Records.vue';
import Goals from './Goals.vue';
import Workouts from "./Workouts.vue";
import Activities from "./Activities.vue";
import Navbar from "./Navbar.vue";
import Buildinfo from "./Buildinfo.vue";
import 'moment';
import 'moment-timezone';
import VueResource from 'vue-resource'
import mixins from './mixins';

Vue.use(VueResource);
Vue.use(VueRouter);
Vue.mixin(mixins);
Vue.component("navbar", Navbar);
Vue.component("buildinfo", Buildinfo);

const routes = [
    { path: '/records', component: Records},
    { path: '/goals', component: Goals},
    { path: '/workouts', component: Workouts},
    { name: 'workouts', path: '/workouts/:date', component: Workouts},
    { path: '/activities', component: Activities},
    { path: '/', component: App},
];

const router = new VueRouter({routes});

new Vue({
    el: "#app",
    router: router
});


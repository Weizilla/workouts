import Vue from 'vue';
import Router from 'vue-router';
import Main from '@/components/Main';
import Records from '@/components/Records';
import Goals from '@/components/Goals';
import Workouts from '@/components/Workouts';
import Activities from '@/components/Activities';

Vue.use(Router);

export default new Router({
  routes: [
      { path: '/records', component: Records},
      { path: '/goals', component: Goals},
      { path: '/workouts', component: Workouts},
      { name: 'workouts', path: '/workouts/:date', component: Workouts},
      { path: '/activities', component: Activities},
      { path: '/', component: Main},
  ],
});

// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import App from './App';
import router from './router';
import VueResource from "vue-resource";
import mixins from "./mixins";
import "bootswatch/dist/cosmo/bootstrap.min.css"
import Vuex from "vuex";
import { store } from "./store/store";

Vue.config.productionTip = false;

Vue.use(VueResource);
Vue.mixin(mixins);

/* eslint-disable no-new */
new Vue({
  el: '#app',
  store,
  router,
  components: { App },
  template: '<App/>',
});

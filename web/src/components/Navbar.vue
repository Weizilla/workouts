<template>
  <div class="navbar navbar-expand-lg">
    <ul class="nav nav-pills">
      <li class="nav-item">
        <router-link class="nav-link" v-bind:class="isActive('/')" to="/">Home</router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" v-bind:class="isActive('/records')" to="/records">Records</router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" v-bind:class="isActive('/goals')" to="/goals">Goals</router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" v-bind:class="isActive('/workouts')" to="/workouts">Workouts</router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" v-bind:class="isActive('/activities')" to="/activities">Activities</router-link>
      </li>
    </ul>
  </div>
</template>

<script>
  export default {
      methods: {
          isActive: function (path) {
              return path === this.$route.path ? "active bg-dark" : "";
          }
      }
  }
</script>


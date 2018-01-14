<template>
  <div>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <a class="navbar-brand" href="#">Workouts</a>

      <div class="collapse navbar-collapse">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item">
            <router-link class="nav-link" to="/">Home</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/records">Records</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/goals">Goals</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/workouts">Workouts</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/activities">Activities</router-link>
          </li>
        </ul>
      </div>
    </nav>
  </div>
</template>

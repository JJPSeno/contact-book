<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <header>
    <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="125" height="125" />
    <div class="wrapper">
      <h1>Contact Book</h1>
      <nav>
        <div class="router-link">
        <RouterLink to="/">Contacts</RouterLink>
        </div> 
        <div class="router-link">
        <RouterLink to="/groups">Groups</RouterLink>
      </div>
      </nav>
    </div>
  </header>
  <main>
  <RouterView />
</main>
</template>

<style scoped>
header{
  display: flex;
  justify-content: center;
  align-items: center;
}

main{
  padding: 4vw;
}

.router-link{
  margin-right: 5px;
}

</style>

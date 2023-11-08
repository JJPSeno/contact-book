<template>
  <li :class="listItemClass">
  <slot/>
  </li>
</template>
<script setup lang="ts">
const props = defineProps({
  listItemClass: String,
})
</script>
<style scoped>
</style>
<template>
  <cb-label :for="inputLabeledId">
    <slot/>
    <cb-input     
      :inputId="inputLabeledId"
      :inputType="inputLabeledType" 
      :inputClass="inputLabeledClass"
      :value="modelValue"
      @input="$emit('update:modelValue', $event.target.value)"
      /> 
  </ps-label>
</template>
<script setup lang="ts">
import cbInput from '../atoms/cb-input.vue';
import cbLabel from '../atoms/cb-label.vue';

defineProps({
  inputLabeledId: String,
  inputLabeledType: String,
  inputLabeledClass: String,
  inputLabeledPlaceholder: String,
  modelValue: String
})
defineEmits(['update:modelValue'])

</script>
<style scoped></style>
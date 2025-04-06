<script setup lang="ts">
import ToggleButton from './ToggleButton.vue';
import { ref, onMounted } from 'vue';

const props = defineProps({
  label: {
    type: String,
    required: false,
  },
  names: {
    type: Array as () => string[],
    required: true,
  },
  direction: {
    type: String,
    default: 'row',
    validator: (value: string) => ['row', 'column'].includes(value),
  },
  autoSelectFirst: {
    type: Boolean,
    default: false,
    required: false
  },
  allowDeselect: {
    type: Boolean,
    default: false,
    required: false
  }
});

const emit = defineEmits(['toggle-selected']);

const selectedToggle = ref<string | null>(null);

/**
 * Handles toggle button clicks with optional deselection.
 * If allowDeselect is true and clicking an already selected toggle,
 * it will deselect it. Otherwise, it just selects the clicked toggle.
 *
 * @param {string} name - The name of the clicked toggle
 */
 function handleToggleClick(name: string) {
  if (props.allowDeselect && selectedToggle.value === name) {
    selectedToggle.value = null;
    emit('toggle-selected', null);
  } else if (selectedToggle.value !== name) {
    selectedToggle.value = name;
    emit('toggle-selected', name);
  }
}


/**
 * Selects the first toggle when the component is mounted if autoSelectFirst is true.
 */
 onMounted(() => {
  if (props.autoSelectFirst && props.names.length > 0) {
    handleToggleClick(props.names[0]);
  }
});
</script>

<template>
  <div class="toggle-group" :class="direction">
    <p v-if="props.label">{{ props.label }}</p>
    <ToggleButton
    v-for="(name, index) in props.names"
    :key="index"
    :name="name"
    :selected="name === selectedToggle"
    @clicked-toggle="handleToggleClick"
    />
    </div>
</template>

<style scoped>
.toggle-group {
  display: flex;
  gap: 8px;
}

.toggle-group.row {
  flex-direction: row;
  align-items: center;
}

.toggle-group.column {
  flex-direction: column;
  align-items: flex-start;
}

p {
  color: #757575;
  white-space: nowrap;
}
</style>

<script setup lang="ts">
import type { AddItemRequest } from '@/models/user.ts';
import { ref, computed, onMounted, watch } from 'vue';
import { useItemStore } from '@/stores/itemStore.ts';
import ToggleGroup from '@/components/Result/ToggleGroup.vue';

const itemStore = useItemStore();

// Add prop to accept existing item for editing
const props = defineProps<{
  existingItem?: AddItemRequest
}>();

const emit = defineEmits<{
  submit: [item: AddItemRequest]
}>();

// Form data - initialize with existing item if provided
const item = ref<AddItemRequest>({
  name: props.existingItem?.name || '',
  description: props.existingItem?.description || '',
  price: props.existingItem?.price || 0,
  category: props.existingItem?.category || ''
});

// Watch for changes to existingItem prop
watch(() => props.existingItem, (newValue) => {
  if (newValue) {
    item.value = { ...newValue };
  }
}, { deep: true });

// Determine if we're in edit mode
const isEditMode = computed(() => !!props.existingItem);

// Only mark fields as touched if in edit mode or when actually touched
const nameTouched = ref(!!props.existingItem);
const descriptionTouched = ref(!!props.existingItem);
const priceTouched = ref(!!props.existingItem);
const categoryTouched = ref(!!props.existingItem);

// Error display element
const errorLabelEl = ref<HTMLElement | null>(null);

// Validation computed properties
const validName = computed(() => item.value.name.trim().length >= 3);
const validDescription = computed(() => item.value.description.trim().length >= 3);
const validPrice = computed(() => typeof item.value.price === 'number' && item.value.price >= 0);
const validCategory = computed(() => item.value.category && item.value.category.trim().length > 0);

// Overall form validity
const validForm = computed(() => {
  return validName.value &&
         validDescription.value &&
         validPrice.value &&
         validCategory.value;
});

/**
 * Handles the click event for a category.
 *
 * @param {string} categoryName - The name of the category that was clicked.
 */
function handleCategoryClick(categoryName: string) {
  item.value.category = categoryName;
  categoryTouched.value = true;
}

/**
 * Sets an error message to be displayed on the error label.
 *
 * @param {string} errorMsg - The error message to display.
 */
function setErrorLabel(errorMsg: string) {
  if (errorLabelEl.value) {
    errorLabelEl.value.textContent = "Submission failed: " + errorMsg;
  }
  console.log("Submission failed: ", errorMsg);
}

/**
 * Handles the submission of the new item listing form.
 * Validates form data and submits it to create a new listing.
 * Triggers form validation before submission and handles any errors.
 */
function submitForm() {
  nameTouched.value = true;
  descriptionTouched.value = true;
  priceTouched.value = true;
  categoryTouched.value = true;

  if (!validForm.value) {
    setErrorLabel("Please fix the errors in the form");
    return;
  }

  const formattedItem = {
    ...item.value,
    price: Number(item.value.price)
  };

  emit('submit', formattedItem);
}

onMounted(() => {
  itemStore.fetchCategories();
});
</script>

<template>
  <div class="item-form-container">
    <form @submit.prevent="submitForm" class="item-form">
      <label for="create-item">{{ isEditMode ? 'Update item details:' : 'Please fill in the item details:' }}</label>

      <div class="form-group">
        <input
          type="text"
          id="name"
          class="input"
          placeholder="Item Name"
          v-model="item.name"
          @blur="nameTouched = true"
          required
        />
        <p v-if="nameTouched && !validName" class="error-msg">
          Name must be at least 3 characters.
        </p>
      </div>

      <div class="form-group">
        <textarea
          id="description"
          class="input"
          placeholder="Description"
          v-model="item.description"
          @blur="descriptionTouched = true"
          required
        ></textarea>
        <p v-if="descriptionTouched && !validDescription" class="error-msg">
          Description must be at least 3 characters.
        </p>
      </div>

      <div class="form-group">
        <input
          type="number"
          id="price"
          class="input"
          placeholder="Price"
          v-model="item.price"
          @blur="priceTouched = true"
          required
        />
        <p v-if="priceTouched && !validPrice" class="error-msg">
          Price cant be negative.
        </p>
      </div>

      <div class="form-group">
        <label for="category">Category:</label>
        <p v-if="itemStore.categoriesError" class="error-msg">
          {{ itemStore.categoriesError }}
        </p>
        <ToggleGroup
          v-else
          :names="itemStore.categories"
          :auto-select-first="!item.category"
          :initial-selected="item.category"
          @toggle-selected="handleCategoryClick"
          direction="row"
          :allow-deselect="false"
          id="category"
        />
        <p v-if="categoryTouched && !validCategory" class="error-msg">
          Please select a category.
        </p>
      </div>

      <input
        type="submit"
        :disabled="!validForm"
        :value="isEditMode ? 'Update Listing' : 'Create Listing'"
        id="submit-button"
      >
      <label for="error" id="item-status-label" ref="errorLabelEl"></label>
    </form>
  </div>
</template>

<style scoped>
.item-form-container {
  display: grid;
  grid-template-rows: 1fr;
  place-items: center;
  border: 1px solid var(--color-border);
  padding: 10px;
  border-radius: 8px;
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
}

.item-form {
  display: grid;
  grid-template-rows: auto;
  place-items: center;
  gap: 16px;
  width: 90%;
}

.form-group {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.input {
  width: 100%;
  padding: 10px;
  border: 1px solid var(--color-border);
  border-radius: 4px;
}

textarea.input {
  min-height: 120px;
  resize: vertical;
}

#submit-button {
  background-color: var(--color-primary);
  color: #FFFFFF;
  border-radius: var(--size-button-radius);
  padding: var(--size-button-padding);
  min-width: var(--size-button-min);
  align-self: center;
  width: auto;
  cursor: pointer;
}

#submit-button:hover {
  background-color: var(--color-primary-hover);
}

#submit-button:disabled {
  background-color: var(--color-background-mute);
  color: var(--color-button-disabled);
  cursor: not-allowed;
}

.error-msg {
  color: red;
  font-size: 0.9em;
  margin: 4px 0 0;
}
</style>

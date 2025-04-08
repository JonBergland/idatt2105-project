<script setup lang="ts">
import type { AddItemRequest } from '@/models/item.ts';
import { ref, computed, onMounted } from 'vue';
import { useItemStore } from '@/stores/itemStore.ts';
import ToggleGroup from '@/components/Result/ToggleGroup.vue';
import * as stringVerificationUtils from '@/utils/stringVerificationUtils';

const itemStore = useItemStore();

const emit = defineEmits<{
  submit: [item: AddItemRequest]
}>();

// Form data
const item = ref<AddItemRequest>({
  name: '',
  description: '',
  price: 0,
  category: ''
});

// Touched states
const nameTouched = ref(false);
const descriptionTouched = ref(false);
const priceTouched = ref(false);
const categoryTouched = ref(false);

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

// Expose for testing
defineExpose({
  item,
  nameTouched,
  descriptionTouched,
  priceTouched,
  categoryTouched,
  validForm,
  submitForm,
  errorLabelEl
});
</script>

<template>
  <div class="item-form-container">
    <form @submit.prevent="submitForm" class="item-form">
      <label for="create-item">Please fill in the item details:</label>

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
          :auto-select-first="true"
          @toggle-selected="handleCategoryClick"
          direction="row"
          :allow-deselect="false"
          id="category"
        />
        <p v-if="categoryTouched && !validCategory" class="error-msg">
          Please select a category.
        </p>
      </div>

      <input type="submit" :disabled="!validForm" value="Create Listing" id="submit-button">
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

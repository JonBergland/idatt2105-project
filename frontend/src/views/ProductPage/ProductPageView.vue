<script setup lang="ts">
import ProductNameComponent from '@/components/ProductPage/ProductNameComponent.vue'
import { onMounted, ref,  } from 'vue';

const props = defineProps<{
  productId: string
}>()

const productName = ref("")
const productState = ref("")
const productComment = ref("")
const sellerName = ref("")
const sellerLocation = ref("")

async function loadProduct(productId:string) {
  // Retrive information from API-call
  // TODO: Fix api-call

  // Imitate API-call
  productName.value = "Product name"
  productState.value = "Good"
  productComment.value = "In a good condition. Hardly used"
  sellerName.value = "Ola Nordman"
  sellerLocation.value = "Bloksberg"

}

onMounted(() => {
  loadProduct(props.productId)
})

</script>

<template>
  <div class="product-page-wrapper">
    <ProductNameComponent
    :product-name="productName"
    />
  </div>

</template>

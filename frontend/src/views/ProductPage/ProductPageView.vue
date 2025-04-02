<script setup lang="ts">
import ProductImageComponent from '@/components/ProductPage/ProductImageComponent.vue';
import ProductNameComponent from '@/components/ProductPage/ProductNameComponent.vue'
import baseImage from '@/assets/images/base-product-image.png'
import { onMounted, ref,  } from 'vue';
import { useRouter } from 'vue-router'

const props = defineProps<{
  productId: string
}>()

const router = useRouter()

const productName = ref("")
const productState = ref("")
const productComment = ref("")
const sellerName = ref("")
const sellerLocation = ref("")

async function loadProduct(productId:string) {
  // Retrive information from API-call
  // TODO: Fix api-call, use service

  // Imitate API-call
  productName.value = "Product name"
  productState.value = "Good"
  productComment.value = "In a good condition. Hardly used"
  sellerName.value = "Ola Nordman"
  sellerLocation.value = "Bloksberg"

}

function handleBackClick() {
  console.log("Back clicked");

  router.push({ name:'home'}) // Adjust as needed
}

onMounted(() => {
  loadProduct(props.productId)
})

</script>

<template>
  <div class="product-page-wrapper">
    <ProductNameComponent
    :product-name="productName"
    @back-click="handleBackClick"
    />
    <ProductImageComponent
    :images="[baseImage, baseImage]"
    />
  </div>

</template>

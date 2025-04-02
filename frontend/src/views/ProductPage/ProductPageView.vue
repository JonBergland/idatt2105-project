<script setup lang="ts">
import ProductImageComponent from '@/components/ProductPage/ProductImageComponent.vue';
import ProductNameComponent from '@/components/ProductPage/ProductNameComponent.vue'
import baseImage from '@/assets/images/base-product-image.png'
import { onMounted, ref,  } from 'vue';
import { useRouter } from 'vue-router'
import ProductInfoComponent from '@/components/ProductPage/ProductInfoComponent.vue';

const props = defineProps<{
  productId: string
}>()

const router = useRouter()

const productName = ref("")
const productAvailability = ref(true)
const productPrice = ref(0)
const productState = ref("")
const productComment = ref("")
const sellerName = ref("")
const sellerLocation = ref("")

async function loadProduct(productId:string) {
  // Retrive information from API-call
  // TODO: Fix api-call, use service

  // Imitate API-call
  productName.value = "Product name"
  productAvailability.value = true
  productPrice.value = 300
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
    <div class="product-info-wrapper">
      <div class="product-image-wrapper">
        <ProductImageComponent
        id="product-image-component"
        :images="[baseImage, baseImage]"
        />
     </div>
     <div class="product-info-component-wrapper">
        <ProductInfoComponent
        :is-available="productAvailability"
        :price="productPrice"
        :state="productState"
        :comment="productComment"
        :location="sellerLocation"
        :seller-name="sellerName"
        />
      </div>
    </div>
  </div>

</template>

<style scoped>

.product-page-wrapper {
  margin: 10px;
}

.product-info-wrapper {
  display: flex;
  gap: 20px;
  align-items: flex-start;
  width: 100%;
  margin-top: 20px;
}

.product-image-wrapper {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  max-width: 50%;
}

.product-image-wrapper > * {
  width: 60%;
}

.product-info-component-wrapper {
  flex: 1;
  max-width: 50%;
}
</style>

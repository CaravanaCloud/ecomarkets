<script lang="ts">
  import { selected_product, show_info } from '../stores';
  import infoIcon from '$lib/assets/info-icon.png';

  export let product: any;

  let image = new URL(product.imagePath, import.meta.url).href

  let nameTextSize = "text-3xl"

  if (product.name.length >= 29) {
    nameTextSize = "text-xl"
  } else if (product.name.length >= 15) {
    nameTextSize = "text-2xl"
  }

  const handleInfoClick = () => {
    show_info.set(true)
    selected_product.set(product)
  }
</script>

<div class="flex flex-col justify-between items-center bg-white w-[13%] h-[28%] min-h-[281px] min-w-[248px] mx-2 rounded-md shadow-md hover:shadow-xl">
  {#if product.imagePath != undefined}
    <img src={image} alt={`Imagem de ${product.name}`} class="rounded-t-md" />
  {/if}

  <div class="flex flex-col items-center w-[100%]">
    <div class="flex items-end justify-around w-[100%]">
      <p class="font-bold {nameTextSize}">{product.name}</p>
      <p class="text-slate-400 text-lg">{product.measureUnit}</p>
    </div>
    
    <p class="text-4xl">{`R$${product.price.unit},${product.price.cents}`}</p>
  </div>

  <div class="flex justify-around w-[100%] m-1">
    <button on:click={handleInfoClick} class="bg-slate-400"><img src={infoIcon} alt="Informações" class="w-[36px]"/></button>
    <button class="bg-lime-500 hover:bg-lime-600 p-1 rounded-md">Adicionar à cesta</button>
  </div>
</div>

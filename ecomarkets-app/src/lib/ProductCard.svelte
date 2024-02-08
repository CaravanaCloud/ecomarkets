<script lang="ts">
  export let product: any;
  export let imageSrc: string;

  let image = new URL(imageSrc, import.meta.url).href

  let nameTextSize = "text-3xl"

  if (product.name.length >= 29) {
    nameTextSize = "text-xl"
  } else if (product.name.length >= 15) {
    nameTextSize = "text-2xl"
  }
</script>

<div class="flex flex-col justify-between items-center bg-white w-[13%] h-[28%] min-h-[281px] min-w-[248px] mx-2 rounded-md shadow-md hover:shadow-xl">
  {#if imageSrc != undefined}
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
    <button class="bg-slate-400">[i]</button>
    <button class="bg-lime-500 p-1 rounded-md">Adicionar à cesta</button>
  </div>
</div>
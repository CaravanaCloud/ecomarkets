<script>
  import { selected_category } from '../../stores';
  import CategoriesBar from '$lib/CategoriesBar.svelte';
  import ProductCard from '$lib/ProductCard.svelte';

  export let data; 
</script>

<CategoriesBar categories={data.categories} />

<div class="grid gap-y-6 grid-cols-7 max-[1811px]:grid-cols-6 max-[1565px]:grid-cols-5 max-[1340px]:grid-cols-4 max-[1070px]:grid-cols-3">
  {#each data.products as product}
    {#if product.category == $selected_category}
      <ProductCard {product} imageSrc=undefined/>
    {/if}
  {/each}
</div>
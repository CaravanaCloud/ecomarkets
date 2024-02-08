<script>
  import CategoriesBar from '$lib/CategoriesBar.svelte';

  export let data; 
</script>

<CategoriesBar categories={data.categories} />
<script>
  import CategoriesBar from '$lib/CategoriesBar.svelte';

  let categories = [
    {name: "Testando", is_selected: true},
    {name: "Mais um teste", is_selected: false},
    {name: "É um teste", is_selected: false},
    {name: "Teste", is_selected: false},
  ]
</script>

<CategoriesBar {categories} />
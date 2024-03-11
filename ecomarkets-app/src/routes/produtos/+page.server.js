export async function load() {
  // Default to test categories
  let categories = [
    {name: "Bebidas", is_selected: true},
    {name: "Frutas", is_selected: false},
    {name: "Caseiros", is_selected: false},
    {name: "Leites e Derivados", is_selected: false},
    {name: "Mel", is_selected: false},
  ]

  let products = [
    {name: "Teste", measureUnit: "KG", description: "Esse é um teste", recipeIngredients: "Teste", price: {unit: 1, cents: 20}, category: "Bebidas"},
    {name: "Chocolate com name gigantesco", measureUnit: "UNIT", description: "Chocolate muito bom", recipeIngredients: "Cacau", price: {unit: 1, cents: 20}, category: "Caseiros"},
    {name: "Testando", measureUnit: "KG", description: "Outro teste", recipeIngredients: "50% teste", price: {unit: 13, cents: 0}, category: "Bebidas"},
    {name: "Banana", measureUnit: "KG", price: {unit: 2, cents: 10}, imagePath: 'assets/banana.jpg', category: "Frutas"}
  ]

  try {
    categories = await fetch('http://localhost:9090/api/category')
      .then((res) => res.json())

    products = await fetch('http://localhost:9090/api/product')
      .then((res) => res.json())
  } catch (error) {
  }

  return { categories, products }
}

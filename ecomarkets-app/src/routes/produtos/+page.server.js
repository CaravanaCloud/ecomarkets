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
    {name: "Teste", measureUnit: "KG", price: {unit: 1, cents: 20}, category: "Bebidas"},
    {name: "Chocolate com name gigantesco", measureUnit: "KG", price: {unit: 1, cents: 20}, category: "Caseiros"}
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
export async function load() {
  // Default to test categories
  let categories = [
    {name: "Bebidas", is_selected: true},
    {name: "Frutas", is_selected: false},
    {name: "Caseiros", is_selected: false},
    {name: "Leites e Derivados", is_selected: false},
    {name: "Mel", is_selected: false},
  ]

  try {
    categories = await fetch('http://localhost:9090/api/category')
      .then((res) => res.json())
  } catch (error) {
  }

  return { categories }
}
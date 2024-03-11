import { writable } from 'svelte/store';

export const cat_store = writable([])
export const selected_category = writable("Bebidas")
export const show_info = writable(false)
export const selected_product = writable()

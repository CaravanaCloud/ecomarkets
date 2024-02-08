import { writable } from 'svelte/store';

export const cat_store = writable([])
export const selected_category = writable("Bebidas")
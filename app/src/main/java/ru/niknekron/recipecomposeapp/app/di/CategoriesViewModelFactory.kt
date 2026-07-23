package ru.niknekron.recipecomposeapp.app.di

import ru.niknekron.recipecomposeapp.data.repository.RecipesRepository
import ru.niknekron.recipecomposeapp.features.categories.presentation.model.CategoriesViewModel

class CategoriesViewModelFactory(
    private val repository: RecipesRepository,
) : Factory<CategoriesViewModel> {

    override fun create(): CategoriesViewModel {
        return CategoriesViewModel(
            repository = repository
        )
    }
}

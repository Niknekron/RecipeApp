package ru.niknekron.recipecomposeapp.app.di

import androidx.lifecycle.SavedStateHandle
import ru.niknekron.recipecomposeapp.data.repository.RecipesRepository
import ru.niknekron.recipecomposeapp.features.recipes.presentation.RecipesViewModel

class RecipesViewModelFactory(
    private val savedStateHandle: SavedStateHandle,
    private val repository: RecipesRepository,
) : Factory<RecipesViewModel> {

    override fun create(): RecipesViewModel {
        return RecipesViewModel(
            savedStateHandle = savedStateHandle,
            repository = repository
        )
    }
}
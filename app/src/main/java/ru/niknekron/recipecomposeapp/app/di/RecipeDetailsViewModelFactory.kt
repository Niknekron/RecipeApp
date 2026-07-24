package ru.niknekron.recipecomposeapp.app.di

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import ru.niknekron.recipecomposeapp.data.repository.RecipesRepository
import ru.niknekron.recipecomposeapp.features.details.presentation.model.RecipeDetailsViewModel

class RecipeDetailsViewModelFactory(
    private val application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val repository: RecipesRepository,
) : Factory<RecipeDetailsViewModel> {
    override fun create(): RecipeDetailsViewModel {
        return RecipeDetailsViewModel(
            application = application,
            savedStateHandle = savedStateHandle,
            repository = repository,
        )
    }
}

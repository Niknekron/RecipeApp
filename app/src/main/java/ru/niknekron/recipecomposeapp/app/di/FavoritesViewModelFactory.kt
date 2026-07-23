package ru.niknekron.recipecomposeapp.app.di

import android.app.Application
import ru.niknekron.recipecomposeapp.data.repository.RecipesRepository
import ru.niknekron.recipecomposeapp.features.favorites.presentation.model.FavoritesViewModel

class FavoritesViewModelFactory(
    private val application: Application,
    private val repository: RecipesRepository,
) : Factory<FavoritesViewModel> {

    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(
            application = application,
            repository = repository
        )
    }
}
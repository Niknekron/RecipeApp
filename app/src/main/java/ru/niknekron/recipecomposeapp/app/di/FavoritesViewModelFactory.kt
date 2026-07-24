package ru.niknekron.recipecomposeapp.app.di

import android.app.Application
import ru.niknekron.recipecomposeapp.data.repository.RecipesRepository
import ru.niknekron.recipecomposeapp.features.favorites.presentation.model.FavoriteViewModel

class FavoritesViewModelFactory(
    private val application: Application,
    private val repository: RecipesRepository,
) : Factory<FavoriteViewModel> {

    override fun create(): FavoriteViewModel {
        return FavoriteViewModel(
            application = application,
            repository = repository
        )
    }
}
package ru.niknekron.recipecomposeapp.app.di

    interface Factory<T> {
        fun create(): T
    }
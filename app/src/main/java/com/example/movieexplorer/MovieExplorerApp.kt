package com.example.movieexplorer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// @HiltAndroidApp sets up dependency injection for the entire app.
@HiltAndroidApp
class MovieExplorerApp : Application()
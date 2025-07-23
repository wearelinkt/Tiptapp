package iq.tiptapp.login

import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

@Composable
expect fun Login(prefs: DataStore<Preferences>)
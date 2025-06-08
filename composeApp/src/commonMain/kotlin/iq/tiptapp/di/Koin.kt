package iq.tiptapp.di

import iq.tiptapp.ui.help.PermissionViewModel
import iq.tiptapp.ui.help.HelpViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(config: KoinAppDeclaration? = null, modules: List<Module> = emptyList()) {
    startKoin {
        config?.invoke(this)
        modules(
            viewModelModule + modules
        )
    }
}

val viewModelModule = module {
    viewModelOf(::PermissionViewModel)
    viewModelOf(::HelpViewModel)
}
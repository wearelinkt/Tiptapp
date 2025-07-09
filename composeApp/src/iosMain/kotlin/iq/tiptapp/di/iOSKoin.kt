package iq.tiptapp.di

import iq.tiptapp.help.HelpViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

fun getIosModules(): List<Module> = listOf(iosViewModelModule)

val iosViewModelModule = module {
    factory { HelpViewModel() }
}
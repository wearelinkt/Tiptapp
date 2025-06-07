package iq.tiptapp.di

import iq.tiptapp.ActivityHolder
import iq.tiptapp.PhoneAuthService
import iq.tiptapp.repository.AndroidAuthService
import iq.tiptapp.ui.verification.VerificationViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun getAndroidModules(): List<Module> = listOf(phoneAuthModule, androidViewModelModule)

val phoneAuthModule = module {
    single<PhoneAuthService> { AndroidAuthService(ActivityHolder.activity) }
}


val androidViewModelModule = module {
    viewModelOf(::VerificationViewModel)
}

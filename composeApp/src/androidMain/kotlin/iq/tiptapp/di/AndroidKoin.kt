package iq.tiptapp.di

import iq.tiptapp.ActivityHolder
import iq.tiptapp.domain.repository.PhoneAuthService
import iq.tiptapp.data.repository.AndroidAuthService
import iq.tiptapp.data.repository.UserRepositoryImpl
import iq.tiptapp.domain.repository.UserRepository
import iq.tiptapp.ui.verification.VerificationViewModel
import iq.tiptapp.ui.splash.SplashViewModel
import iq.tiptapp.help.HelpViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun getAndroidModules(): List<Module> = listOf(phoneAuthModule, androidViewModelModule, repositoryModule)

val phoneAuthModule = module {
    single<PhoneAuthService> { AndroidAuthService(ActivityHolder.activity) }
}


val androidViewModelModule = module {
    viewModelOf(::VerificationViewModel)
    viewModelOf(::SplashViewModel)
    viewModelOf(::HelpViewModel)
}

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
}

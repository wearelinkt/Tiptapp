package iq.tiptapp.di

import iq.tiptapp.ActivityHolder
import iq.tiptapp.repository.AndroidAuthService
import iq.tiptapp.PhoneAuthService
import iq.tiptapp.ui.verification.VerificationViewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            repositoryModule,
            viewModelModule
        )
    }
}

val repositoryModule = module {
    single<PhoneAuthService> { AndroidAuthService(ActivityHolder.activity) }
}


val viewModelModule = module {
    viewModelOf(::VerificationViewModel)
}

/*
val dispatcherModule = module {
    single(named("io")) { Dispatchers.IO }
}

val persistenceModule = module {
    single { get<AppDatabase>().movieDao() }
}*/

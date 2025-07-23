package iq.tiptapp.di

import iq.tiptapp.camera.CameraPermissionViewModel
import iq.tiptapp.data.network.jsonModule
import iq.tiptapp.data.network.ktorModule
import iq.tiptapp.data.repository.UserRepositoryImpl
import iq.tiptapp.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(config: KoinAppDeclaration? = null, modules: List<Module> = emptyList()) {
    startKoin {
        config?.invoke(this)
        modules(
            listOf(
                ktorModule,
                jsonModule,
                dispatcherModule,
                viewModelModule,
                repositoryModule
            ) + modules
        )
    }
}

val viewModelModule = module {
    viewModelOf(::CameraPermissionViewModel)
}

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
}

val dispatcherModule = module {
    single(named("io")) { Dispatchers.IO }
}
package com.mrifdnp.gluvia.di

import HomeViewModel
import com.mrifdnp.gluvia.SupabaseClientProvider
import com.mrifdnp.gluvia.data.AuthRepository
import com.mrifdnp.gluvia.data.ProfileRepository
import com.mrifdnp.gluvia.ui.viewmodel.MainViewModel
import com.mrifdnp.gluvia.ui.viewmodel.ProfileViewModel
import com.mrifdnp.gluvia.ui.viewmodel.SignInViewModel

import com.mrifdnp.gluvia.ui.viewmodel.SignUpViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module



val appModule = module {
    // Register SupabaseClientProvider as a Singleton
    single { SupabaseClientProvider.client }
    single { AuthRepository(supabaseClient = get()) }
    single { ProfileRepository(supabaseClient = get(), authRepository = get()) }

    // 3. SignUpViewModel (Gunakan factory atau viewModel)
    viewModel { MainViewModel(authRepository = get()) }
    viewModel { SignUpViewModel(signUpRepository = get()) }
    viewModel { SignInViewModel(authRepository = get()) }
    viewModel {
        HomeViewModel(
            authRepository = get() // HomeViewModel membutuhkan AuthRepository
        )
    }
    viewModel { ProfileViewModel(profileRepository = get()) }

}
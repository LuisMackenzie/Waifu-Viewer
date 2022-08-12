package com.mackenzie.waifuviewer.ui.favs

import androidx.lifecycle.ViewModel
import com.mackenzie.waifuviewer.ui.common.Scope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(): ViewModel(), Scope by Scope.Impl() {



}
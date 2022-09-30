package com.mackenzie.waifuviewer.ui.common

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob

interface Scope : CoroutineScope {

    var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    class Impl : Scope {
        override lateinit var job: Job
    }

    fun initScope() {
        job = SupervisorJob()
    }

    fun destroyScope() {
        job.cancel()
    }
}
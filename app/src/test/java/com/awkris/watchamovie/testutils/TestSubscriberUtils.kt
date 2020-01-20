@file:JvmName("TestSubscriberUtils")

package com.awkris.watchamovie.testutils

import io.reactivex.observers.TestObserver
import org.junit.Assert

fun TestObserver<*>.assertCompleteWithoutErrors() {
    assertNoErrors()
    assertComplete()
}

fun TestObserver<*>.assertSuccessObserver(expectedOnNextCalling: Int) {
    assertNoErrors()
    Assert.assertEquals(expectedOnNextCalling, values().size)
    assertComplete()
}
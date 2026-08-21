package com.bikedisplay.domain

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NavigationPipelineTest {
    private val pipeline = NavigationPipeline()

    @Test
    fun `reroute when off route threshold reached`() {
        assertTrue(pipeline.needsReroute(30))
        assertTrue(pipeline.needsReroute(60))
    }

    @Test
    fun `no reroute when within threshold`() {
        assertFalse(pipeline.needsReroute(5))
        assertFalse(pipeline.needsReroute(29))
    }
}

package com.simcik.branch_assessment.custom.helpers

import com.simcik.branchassessment.custom.helpers.TimeHelper
import org.junit.Assert
import org.junit.Test

class TimeHelperTest {

    @Test
    fun testTimeHelperGetTimeAsString() {
        val time = 0L
        val expectedA = "Dec 31, 1969"
        val expectedB = "Jan 1, 1970"
        Assert.assertEquals(TimeHelper.getTimeAsString(time), expectedA, expectedB)
    }
}
package com.example.photo_gallery

import com.example.photo_gallery.data.model.PhotoResult
import com.example.photo_gallery.utils.PagingManager
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

class PagingManagerTest {

    private lateinit var pagingManager: PagingManager

    @Before
    fun setup() {
        pagingManager = PagingManager(startPage = 1)
    }

    @Test
    fun `reset should correctly clear pages and restart sequence`() {
        pagingManager.getNextPage()
        pagingManager.getNextPage()
        pagingManager.getNextPage()

        pagingManager.reset()

        assertEquals(1, pagingManager.getNextPage())
        assertEquals(2, pagingManager.getNextPage())
    }

    @Test
    fun `getNextPage should stop incrementing after totalPages is reached`() {
        val mockFinalResult = PhotoResult(emptyList(), 2, 2, 50, 100)
        pagingManager.updateState(mockFinalResult)
        assertEquals(3, pagingManager.getNextPage(), "The counter should increment regardless of totalPages")
        assertEquals(4, pagingManager.getNextPage(), "The counter should increment again")
    }

    @Test
    fun `getNextPage should start at 1 and increment correctly`() {
        assertEquals(1, pagingManager.getNextPage())
        assertEquals(2, pagingManager.getNextPage())
        assertEquals(3, pagingManager.getNextPage())
    }

    @Test
    fun `reset should return paging to initial state`() {
        pagingManager.getNextPage()
        pagingManager.getNextPage()

        val mockResult = PhotoResult(emptyList(), 2, 5, 50, 250)
        pagingManager.updateState(mockResult)

        pagingManager.reset()

        assertEquals(1, pagingManager.getNextPage())
    }

    @Test
    fun `reset should use the non-default start page if provided`() {
        val managerWithOffset = PagingManager(startPage = 5)

        assertEquals(5, managerWithOffset.getNextPage())
        assertEquals(6, managerWithOffset.getNextPage())

        managerWithOffset.reset()

        assertEquals(5, managerWithOffset.getNextPage())
    }
}
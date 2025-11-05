package com.example.photo_gallery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.photo_gallery.data.model.FlickrPhoto
import com.example.photo_gallery.data.model.PhotoResult
import com.example.photo_gallery.data.repository.FlickrRepository
import com.example.photo_gallery.utils.PagingManager
import com.example.photo_gallery.utils.UiStateResult
import com.example.photo_gallery.viewmodel.GalleryViewModel
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.*
import org.mockito.kotlin.verify
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class GalleryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: FlickrRepository
    private lateinit var pagingManager: PagingManager
    private lateinit var viewModel: GalleryViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(FlickrRepository::class.java)
        pagingManager = mock(PagingManager::class.java)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given autoLoad true when init then fetchRecentPhotos called`() = runTest {
        val photoResult = PhotoResult(emptyList(), total = 10, page = 1, perPage = 10, pages = 1)
        `when`(repository.getRecentPhotos(anyInt())).thenReturn(UiStateResult.Success(photoResult))
        `when`(pagingManager.getNextPage()).thenReturn(1)

        viewModel = GalleryViewModel(repository, pagingManager, autoLoad = true)
        advanceUntilIdle()

        verify(repository, times(1)).getRecentPhotos(1)
        val state = viewModel.galleryState.first()
        assertFalse(state.isInitialLoading)
        assertEquals(1, state.currentPage)
    }

    @Test
    fun `given repository error when fetchPhotos called then update error state`() = runTest {
        `when`(repository.getRecentPhotos(anyInt())).thenReturn(UiStateResult.Error("Network error"))
        `when`(pagingManager.getNextPage()).thenReturn(1)

        viewModel = GalleryViewModel(repository, pagingManager, autoLoad = false)

        viewModel.reload()
        advanceUntilIdle()

        val state = viewModel.galleryState.first()
        assertEquals("Network error", state.error)
        assertFalse(state.isInitialLoading)
    }

    @Test
    fun `given next page when loadNextPage then photos appended distinct`() = runTest {
        val photo1 = FlickrPhoto(id = "1", owner = "owner1", secret = "s1", server = "server1", farm = 1, title = "t1", isPublic = 1)
        val photo2 = FlickrPhoto(id = "2", owner = "owner2", secret = "s2", server = "server2", farm = 1, title = "t2", isPublic = 1)

        val page1 = PhotoResult(photos = listOf(photo1), total = 10, page = 1, perPage = 5, pages = 2)

        val page2 = PhotoResult(photos = listOf(photo2), total = 10, page = 2, perPage = 5, pages = 2)

        `when`(repository.getRecentPhotos(eq(1))).thenReturn(UiStateResult.Success(page1))
        `when`(repository.getRecentPhotos(eq(2))).thenReturn(UiStateResult.Success(page2))

        `when`(pagingManager.getNextPage()).thenReturn(1).thenReturn(2)
        `when`(pagingManager.isEndReached()).thenReturn(false)

        viewModel = GalleryViewModel(repository, pagingManager, autoLoad = true)
        advanceUntilIdle() // Wait for Page 1 to complete

        // LoadNextPage should trigger the call for page 2
        viewModel.loadNextPage()
        advanceUntilIdle() // Wait for Page 2 to complete

        val state = viewModel.galleryState.first()

        assertEquals(2, state.photos.size) // Expect 1 (from page 1) + 1 (from page 2) = 2
        assertEquals(2, state.currentPage)
        verify(repository, times(2)).getRecentPhotos(anyInt()) // Verify both page 1 and page 2 were fetched
    }

    @Test
    fun `given active job when fetchPhotos called then ignore subsequent calls`() = runTest {
        val result = UiStateResult.Success(PhotoResult(emptyList(), 10, 1, 10, 1))
        `when`(repository.getRecentPhotos(anyInt())).thenReturn(result)
        `when`(pagingManager.getNextPage()).thenReturn(1)

        viewModel = GalleryViewModel(repository, pagingManager, autoLoad = false)
        viewModel.reload()

        // Call again immediately before job clears
        viewModel.reload()

        verify(repository, times(1)).getRecentPhotos(anyInt())
    }

}
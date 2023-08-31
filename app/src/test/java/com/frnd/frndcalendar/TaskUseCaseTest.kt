package com.frnd.frndcalendar

import com.frnd.frndcalendar.domain.TaskUseCase
import com.frnd.frndcalendar.remote.model.StoreTaskRequest
import com.frnd.frndcalendar.remote.model.Task
import com.frnd.frndcalendar.remote.repository.TaskRemoteRepository
import com.frnd.frndcalendar.storage.repository.TaskLocalRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class TaskUseCaseTest {

    @Mock
    private lateinit var remoteRepository: TaskRemoteRepository

    @Mock
    private lateinit var taskRepository: TaskLocalRepository

    private lateinit var taskUseCase: TaskUseCase

    @Before
    fun setup() {
        taskUseCase = TaskUseCase(remoteRepository, taskRepository)
    }

    @Test
    fun `storeCalendarTask calls remoteRepository storeCalendarTask`() = runBlockingTest {
        val request = StoreTaskRequest(12, Task("title","description"))
        val response:Response<Any> = Response.success(StoreTaskRequest(13, Task("title","description")))
        Mockito.`when`(remoteRepository.storeCalendarTask(request))
            .thenReturn(response)

        val result = taskUseCase.storeCalendarTask(request)
        assertEquals(response, result)
    }

}
